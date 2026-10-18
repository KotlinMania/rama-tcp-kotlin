// port-lint: source client/service/connector.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramanet.TransportProtocol
import io.github.kotlinmania.ramanet.address.ProxyAddress
import io.github.kotlinmania.ramanet.client.ConnectorTarget
import io.github.kotlinmania.ramanet.client.EstablishedClientConnection
import io.github.kotlinmania.ramanet.stream.ClientSocketInfo
import io.github.kotlinmania.ramanet.stream.SocketInfo
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.client.DefaultDnsResolver
import io.github.kotlinmania.ramatcp.client.DefaultTcpStreamConnector
import io.github.kotlinmania.ramatcp.client.DnsResolver
import io.github.kotlinmania.ramatcp.client.Request
import io.github.kotlinmania.ramatcp.client.TcpStreamConnector
import io.github.kotlinmania.ramatcp.client.tcpConnect
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

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
    @HiddenFromObjC
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
            stream.extensionsMut().insert(
                ClientSocketInfo.new(SocketInfo.new(localAddr = stream.localAddr(), peerAddr = addr)),
            )
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
            stream.extensionsMut().insert(
                ClientSocketInfo.new(SocketInfo.new(localAddr = stream.localAddr(), peerAddr = addr)),
            )
            return EstablishedClientConnection(input = request, conn = stream)
        }

        val transportCtx = request.toTransportContext()
        if (transportCtx.protocol == TransportProtocol.Udp) {
            throw IllegalStateException("Tcp Connector Service cannot establish a UDP transport")
        }

        val authority =
            transportCtx.hostWithPort()
                ?: request.authority
        val (stream, addr) =
            tcpConnect(
                extensions = request.extensions(),
                address = authority,
                dns = dns,
                connector = connector,
            )
        stream.extensionsMut().insert(
            ClientSocketInfo.new(SocketInfo.new(localAddr = stream.localAddr(), peerAddr = addr)),
        )
        return EstablishedClientConnection(input = request, conn = stream)
    }

    /**
     * Serve a client connection request.
     */
    @HiddenFromObjC
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

        public typealias Output = ConnectorOutput
        public typealias Error = ConnectorError
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
