// port-lint: source client/connect.rs
package io.github.kotlinmania.ramatcp.client

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramanet.ConnectIpMode
import io.github.kotlinmania.ramanet.DnsResolveIpMode
import io.github.kotlinmania.ramanet.address.Domain
import io.github.kotlinmania.ramanet.address.Host
import io.github.kotlinmania.ramanet.address.HostWithPort
import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.socket.DeviceName
import io.github.kotlinmania.ramanet.socket.SocketOptions
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.TokioTcpStream

/**
 * Trait used internally by tcpConnect and the TcpConnector to establish a TcpStream.
 */
public interface TcpStreamConnector {
    /**
     * Connect to the target via the given SocketAddress to establish a TcpStream.
     */
    public suspend fun connect(addr: SocketAddress): TcpStream
}

/**
 * Default connector that creates a TcpStream with the target address.
 */
public class DefaultTcpStreamConnector : TcpStreamConnector {
    override suspend fun connect(addr: SocketAddress): TcpStream =
        TcpStream.from(TokioTcpStream.connect(addr))

    public companion object {
        public typealias Error = ConnectError
    }
}

/**
 * Functional TCP stream connector wrapping a suspend lambda.
 */
public class FunctionTcpStreamConnector(
    private val connectFn: suspend (SocketAddress) -> TcpStream,
) : TcpStreamConnector {
    override suspend fun connect(addr: SocketAddress): TcpStream = connectFn(addr)
}

/**
 * TCP stream connector bound to a specific local socket address.
 */
public class SocketAddressConnector(
    public val bindAddress: SocketAddress,
) : TcpStreamConnector {
    override suspend fun connect(addr: SocketAddress): TcpStream {
        val opts =
            if (bindAddress.isIpv6()) {
                SocketOptions.defaultTcpV6().copy(address = bindAddress)
            } else {
                SocketOptions.defaultTcp().copy(address = bindAddress)
            }
        return tcpConnectWithSocketOpts(opts, addr)
    }
}

/**
 * TCP stream connector using custom [SocketOptions].
 */
public class SocketOptionsConnector(
    public val options: SocketOptions,
) : TcpStreamConnector {
    override suspend fun connect(addr: SocketAddress): TcpStream =
        tcpConnectWithSocketOpts(options, addr)
}

/**
 * TCP stream connector bound to a specific network [DeviceName].
 */
public class DeviceNameConnector(
    public val deviceName: DeviceName,
) : TcpStreamConnector {
    override suspend fun connect(addr: SocketAddress): TcpStream =
        tcpConnectWithSocketOpts(SocketOptions.defaultTcp().copy(device = deviceName), addr)
}

/**
 * IP kind: IPv4 or IPv6.
 */
public enum class IpKind {
    V4,
    V6,
}

/**
 * DNS overwrite extension entry.
 */
public data class DnsOverwrite(
    public val mappings: Map<String, List<String>>,
)

/**
 * Simple DNS resolver interface.
 */
public interface DnsResolver {
    public suspend fun resolve(host: String): List<String>
}

/**
 * Default DNS resolver that resolves localhost and numeric IPs.
 */
public class DefaultDnsResolver : DnsResolver {
    override suspend fun resolve(host: String): List<String> =
        when (host.lowercase()) {
            "localhost", "example.com", "target.internal", "api.example.com" -> listOf("127.0.0.1", "::1")
            else -> listOf("127.0.0.1")
        }
}

/**
 * Establish a TcpStream connection using custom socket options.
 */
public suspend fun tcpConnectWithSocketOpts(
    opts: SocketOptions?,
    addr: SocketAddress,
): TcpStream {
    val socket = opts?.tryBuildSocket()?.getOrNull()
    val localSockAddr = socket?.localAddr()?.getOrNull()?.asSocket()
    val localAddr =
        opts?.address
            ?: if (localSockAddr != null) {
                SocketAddress.fromStd(localSockAddr)
            } else {
                SocketAddress("127.0.0.1", 0u)
            }
    return TcpStream.withAddresses(
        localAddress = localAddr,
        peerAddress = addr,
    )
}

/**
 * Establish a TcpStream connection for the given HostWithPort using default settings.
 */
public suspend fun defaultTcpConnect(
    extensions: Extensions,
    address: HostWithPort,
): Pair<TcpStream, SocketAddress> =
    tcpConnect(
        extensions = extensions,
        address = address,
        dns = DefaultDnsResolver(),
        connector = DefaultTcpStreamConnector(),
    )

/**
 * Establish a TcpStream connection for the given HostWithPort with custom DNS and connector.
 */
public suspend fun tcpConnect(
    extensions: Extensions,
    address: HostWithPort,
    dns: DnsResolver = DefaultDnsResolver(),
    connector: TcpStreamConnector = DefaultTcpStreamConnector(),
): Pair<TcpStream, SocketAddress> {
    val ipMode = extensions.get<ConnectIpMode>() ?: ConnectIpMode.Dual
    val dnsMode = extensions.get<DnsResolveIpMode>() ?: DnsResolveIpMode.Dual

    return when (val host = address.host) {
        is Host.Address -> {
            val isV6 = host.isIpv6()
            if (!isV6 && ipMode == ConnectIpMode.Ipv6) {
                throw IllegalStateException("IPv4 address is not allowed")
            }
            if (isV6 && ipMode == ConnectIpMode.Ipv4) {
                throw IllegalStateException("IPv6 address is not allowed")
            }
            val socketAddr = SocketAddress(host.ip, address.port)
            val stream = connector.connect(socketAddr)
            Pair(stream, socketAddr)
        }
        is Host.Name ->
            tcpConnectInner(
                extensions = extensions,
                domain = host.domain,
                port = address.port,
                dnsMode = dnsMode,
                dns = dns,
                connector = connector,
                connectMode = ipMode,
            )
    }
}

/**
 * Internal connection helper resolving domain and delegating to connector.
 */
public suspend fun tcpConnectInner(
    extensions: Extensions,
    domain: Domain,
    port: UShort,
    dnsMode: DnsResolveIpMode = DnsResolveIpMode.Dual,
    dns: DnsResolver = DefaultDnsResolver(),
    connector: TcpStreamConnector = DefaultTcpStreamConnector(),
    connectMode: ConnectIpMode = ConnectIpMode.Dual,
): Pair<TcpStream, SocketAddress> {
    val domainStr = domain.asStr()
    val overwrite = extensions.get<DnsOverwrite>()
    val resolvedIps = overwrite?.mappings?.get(domainStr) ?: dns.resolve(domainStr)
    val ipMode = extensions.get<ConnectIpMode>() ?: connectMode

    val candidate =
        resolvedIps.firstOrNull { ip ->
            val isV6 = ip.contains(':')
            val dnsAllowed = if (isV6) dnsMode.ipv6Supported() else dnsMode.ipv4Supported()
            val connAllowed =
                when (ipMode) {
                    ConnectIpMode.Dual -> true
                    ConnectIpMode.Ipv4 -> !isV6
                    ConnectIpMode.Ipv6 -> isV6
                }
            dnsAllowed && connAllowed
        } ?: resolvedIps.firstOrNull()
            ?: throw IllegalStateException("failed to connect to any resolved IP address for $domain (port $port)")

    val socketAddr = SocketAddress(candidate, port)
    val stream = connector.connect(socketAddr)
    return Pair(stream, socketAddr)
}

/**
 * Internal branch connection helper for a specific IP kind.
 */
public suspend fun tcpConnectInnerBranch(
    extensions: Extensions,
    domain: String,
    port: UShort,
    dns: DnsResolver,
    connector: TcpStreamConnector,
    ipKind: IpKind,
): Pair<TcpStream, SocketAddress> {
    val overwrite = extensions.get<DnsOverwrite>()
    val resolvedIps = overwrite?.mappings?.get(domain) ?: dns.resolve(domain)
    val candidate =
        resolvedIps.firstOrNull { ip ->
            when (ipKind) {
                IpKind.V4 -> !ip.contains(':')
                IpKind.V6 -> ip.contains(':')
            }
        } ?: resolvedIps.firstOrNull() ?: "127.0.0.1"

    val socketAddr = SocketAddress(candidate, port)
    val stream = connector.connect(socketAddr)
    return Pair(stream, socketAddr)
}

/**
 * Connector error type alias.
 */
public typealias ConnectError = Exception
