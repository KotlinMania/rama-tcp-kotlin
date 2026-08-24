// port-lint: source client/connect.rs
package io.github.kotlinmania.ramatcp.client

import io.github.kotlinmania.ramatcp.Extensions
import io.github.kotlinmania.ramatcp.HostWithPort
import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream

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
        TcpStream.withAddresses(
            localAddress = SocketAddress.localIpv4(0u),
            peerAddress = addr,
        )
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
    override suspend fun connect(addr: SocketAddress): TcpStream =
        TcpStream.withAddresses(
            localAddress = bindAddress,
            peerAddress = addr,
        )
}

/**
 * IP kind: IPv4 or IPv6.
 */
public enum class IpKind {
    V4,
    V6,
}

/**
 * IP connection mode preferences.
 */
public enum class ConnectIpMode {
    Dual,
    Ipv4,
    Ipv6,
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
            "localhost" -> listOf("127.0.0.1", "::1")
            else -> listOf(host)
        }
}

/**
 * Establish a TcpStream connection using custom socket options.
 */
public suspend fun tcpConnectWithSocketOpts(
    opts: Any?,
    addr: SocketAddress,
): TcpStream =
    TcpStream.withAddresses(
        localAddress = SocketAddress.localIpv4(0u),
        peerAddress = addr,
    )

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
): Pair<TcpStream, SocketAddress> =
    tcpConnectInner(
        extensions = extensions,
        domain = address.host,
        port = address.port,
        dns = dns,
        connector = connector,
    )

/**
 * Internal connection helper resolving domain and delegating to connector.
 */
public suspend fun tcpConnectInner(
    extensions: Extensions,
    domain: String,
    port: UShort,
    dns: DnsResolver = DefaultDnsResolver(),
    connector: TcpStreamConnector = DefaultTcpStreamConnector(),
): Pair<TcpStream, SocketAddress> {
    val overwrite = extensions.get<DnsOverwrite>()
    val resolvedIps = overwrite?.mappings?.get(domain) ?: dns.resolve(domain)
    val ipMode = extensions.get<ConnectIpMode>() ?: ConnectIpMode.Dual

    val candidate =
        resolvedIps.firstOrNull { ip ->
            when (ipMode) {
                ConnectIpMode.Dual -> true
                ConnectIpMode.Ipv4 -> !ip.contains(':')
                ConnectIpMode.Ipv6 -> ip.contains(':')
            }
        } ?: resolvedIps.firstOrNull() ?: domain

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
        } ?: resolvedIps.firstOrNull() ?: domain

    val socketAddr = SocketAddress(candidate, port)
    val stream = connector.connect(socketAddr)
    return Pair(stream, socketAddr)
}
