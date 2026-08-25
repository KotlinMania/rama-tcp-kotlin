// port-lint: source client/service/connector.rs
package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramatcp.HostWithPort
import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.client.DefaultDnsResolver
import io.github.kotlinmania.ramatcp.client.DefaultTcpStreamConnector
import io.github.kotlinmania.ramatcp.client.DnsResolver
import io.github.kotlinmania.ramatcp.client.Request
import io.github.kotlinmania.ramatcp.client.TcpStreamConnector
import io.github.kotlinmania.ramatcp.client.tcpConnect

/**
 * Proxy address extension.
 */
public data class ProxyAddress(
    public val address: HostWithPort,
)

/**
 * Connector target override extension.
 */
public data class ConnectorTarget(
    public val target: HostWithPort,
)

/**
 * Client socket info recording local and peer addresses.
 */
public data class ClientSocketInfo(
    public val local: SocketAddress?,
    public val peer: SocketAddress,
)

/**
 * Result of establishing a client connection.
 */
public data class EstablishedClientConnection<out Stream, out Input>(
    public val input: Input,
    public val conn: Stream,
)

/**
 * A connector which can be used to establish a TCP connection to a server.
 */
public class TcpConnector<Dns : DnsResolver, Factory : TcpStreamConnectorFactory<*>>(
    public val dns: Dns,
    public val connectorFactory: Factory,
) {
    /**
     * Consume this connector to attach the given DNS resolver.
     */
    public fun <OtherDns : DnsResolver> withDns(otherDns: OtherDns): TcpConnector<OtherDns, Factory> =
        TcpConnector(dns = otherDns, connectorFactory = connectorFactory)

    /**
     * Consume this connector to attach the given connector.
     */
    public fun <C : TcpStreamConnector> withConnector(
        connector: C,
    ): TcpConnector<Dns, TcpStreamConnectorCloneFactory<C>> =
        TcpConnector(dns = dns, connectorFactory = TcpStreamConnectorCloneFactory(connector))

    /**
     * Consume this connector to attach the given factory.
     */
    public fun <OtherFactory : TcpStreamConnectorFactory<*>> withConnectorFactory(
        factory: OtherFactory,
    ): TcpConnector<Dns, OtherFactory> =
        TcpConnector(dns = dns, connectorFactory = factory)

    /**
     * Establish a TCP client connection for the given request.
     */
    public suspend fun connect(request: Request): EstablishedClientConnection<TcpStream, Request> {
        val created = connectorFactory.makeConnector()
        val connector = created.connector

        val proxy = request.extensions().get<ProxyAddress>()
        if (proxy != null) {
            val (stream, addr) =
                tcpConnect(
                    extensions = request.extensions(),
                    address = proxy.address,
                    dns = dns,
                    connector = connector,
                )
            stream.extensions().insert(ClientSocketInfo(local = stream.localAddr(), peer = addr))
            return EstablishedClientConnection(input = request, conn = stream)
        }

        val target = request.extensions().get<ConnectorTarget>()
        if (target != null) {
            val (stream, addr) =
                tcpConnect(
                    extensions = request.extensions(),
                    address = target.target,
                    dns = dns,
                    connector = connector,
                )
            stream.extensions().insert(ClientSocketInfo(local = stream.localAddr(), peer = addr))
            return EstablishedClientConnection(input = request, conn = stream)
        }

        val (stream, addr) =
            tcpConnect(
                extensions = request.extensions(),
                address = request.authority,
                dns = dns,
                connector = connector,
            )
        stream.extensions().insert(ClientSocketInfo(local = stream.localAddr(), peer = addr))
        return EstablishedClientConnection(input = request, conn = stream)
    }

    /**
     * Serve a client connection request.
     */
    public suspend fun serve(input: Request): EstablishedClientConnection<TcpStream, Request> =
        connect(input)

    public companion object {
        /**
         * Create a new default TcpConnector.
         */
        public fun new(): TcpConnector<DefaultDnsResolver, TcpStreamConnectorCloneFactory<DefaultTcpStreamConnector>> =
            TcpConnector(
                dns = DefaultDnsResolver(),
                connectorFactory = TcpStreamConnectorCloneFactory(DefaultTcpStreamConnector()),
            )

        /**
         * Create a default TcpConnector.
         */
        public fun default(): TcpConnector<DefaultDnsResolver, TcpStreamConnectorCloneFactory<DefaultTcpStreamConnector>> =
            new()
    }
}

/**
 * Connector output type alias.
 */
public typealias ConnectorOutput = EstablishedClientConnection<TcpStream, Request>

/**
 * Connector error type alias.
 */
public typealias ConnectorError = Exception


