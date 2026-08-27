// port-lint: source client/service/forward.rs
package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramatcp.HostWithPort
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.client.DefaultDnsResolver
import io.github.kotlinmania.ramatcp.client.DefaultTcpStreamConnector
import io.github.kotlinmania.ramatcp.client.Request

/**
 * Proxy target container for dynamic forwarding.
 */
public data class ProxyTarget(
    public val target: HostWithPort,
)

/**
 * Kind of target for TCP forwarding.
 */
public sealed class ForwarderKind {
    /** Static target host and port. */
    public data class Static(
        val target: HostWithPort,
    ) : ForwarderKind()

    /** Dynamic target extracted from context extensions. */
    public data object Dynamic : ForwarderKind()
}

/**
 * A TCP forwarder.
 */
public class Forwarder<C>(
    public val kind: ForwarderKind,
    public val connector: C,
) {
    /**
     * Set a custom connector for this forwarder.
     */
    public fun <T> withConnector(connector: T): Forwarder<T> =
        Forwarder(kind = kind, connector = connector)

    /**
     * Forward connections from source to target.
     */
    public suspend fun serve(source: TcpStream) {
        val authority =
            when (kind) {
                is ForwarderKind.Static -> kind.target
                is ForwarderKind.Dynamic ->
                    source.extensions().get<ProxyTarget>()?.target
                        ?: throw IllegalStateException("missing forward authority")
            }

        val extensions = source.extensions().copy()
        val req = Request.newWithExtensions(authority, extensions)
        if (connector is TcpConnector<*, *>) {
            connector.connect(req)
        }
    }

    public companion object {
        /**
         * Create a new static forwarder for the given target HostWithPort.
         */
        public fun new(target: HostWithPort): DefaultForwarder =
            Forwarder(
                kind = ForwarderKind.Static(target),
                connector = TcpConnector.new(),
            )

        /**
         * Create a new dynamic forwarder which fetches the target from context.
         */
        public fun ctx(): DefaultForwarder =
            Forwarder(
                kind = ForwarderKind.Dynamic,
                connector = TcpConnector.new(),
            )

        public typealias Output = ForwardOutput
        public typealias Error = ForwardError
    }
}

/**
 * Default forwarder type alias using TcpConnector.
 */
public typealias DefaultForwarder = Forwarder<TcpConnector<DefaultDnsResolver, TcpStreamConnectorCloneFactory<DefaultTcpStreamConnector>>>

/**
 * Forward output type alias.
 */
public typealias ForwardOutput = Unit

/**
 * Forward error type alias.
 */
public typealias ForwardError = Exception




