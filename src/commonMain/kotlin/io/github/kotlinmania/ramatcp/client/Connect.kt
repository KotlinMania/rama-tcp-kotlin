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
 * IP connection mode preferences.
 */
public enum class ConnectIpMode {
    Dual,
    Ipv4,
    Ipv6,
}

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
    val host = address.host
    val port = address.port

    val resolvedIps = dns.resolve(host)
    val candidate =
        resolvedIps.firstOrNull { ip ->
            when (ipMode) {
                ConnectIpMode.Dual -> true
                ConnectIpMode.Ipv4 -> !ip.contains(':')
                ConnectIpMode.Ipv6 -> ip.contains(':')
            }
        } ?: resolvedIps.firstOrNull() ?: host

    val socketAddr = SocketAddress(candidate, port)
    val stream = connector.connect(socketAddr)
    return Pair(stream, socketAddr)
}
