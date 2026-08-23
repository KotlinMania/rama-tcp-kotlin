// port-lint: source client/request.rs
package io.github.kotlinmania.ramatcp.client

import io.github.kotlinmania.ramatcp.Extensions
import io.github.kotlinmania.ramatcp.ExtensionsMut
import io.github.kotlinmania.ramatcp.ExtensionsRef
import io.github.kotlinmania.ramatcp.HostWithPort

/**
 * Transport protocol identifier.
 */
public enum class TransportProtocol {
    Tcp,
    Udp,
}

/**
 * Transport context describing connection parameters.
 */
public data class TransportContext(
    public val protocol: TransportProtocol = TransportProtocol.Tcp,
    public val appProtocol: Protocol? = null,
    public val httpVersion: HttpVersion? = null,
    public val authority: HostWithPort,
)

/**
 * Protocol identifier for application layer hint.
 */
public enum class Protocol {
    Http,
    Https,
    Ws,
    Wss,
    Custom,
}

/**
 * HTTP version identifier hint.
 */
public enum class HttpVersion {
    Http09,
    Http10,
    Http11,
    H2,
    H3,
}

/**
 * A request to establish a TCP Connection.
 *
 * This can be used when operating on a layer below an application layer such as HTTP.
 */
public data class Request(
    public var authority: HostWithPort,
    public var protocol: Protocol? = null,
    public var httpVersion: HttpVersion? = null,
    public var extensions: Extensions = Extensions(),
) : ExtensionsRef,
    ExtensionsMut {
    override fun extensions(): Extensions = extensions

    override fun extensionsMut(): Extensions = extensions

    /**
     * Define the application protocol for this request.
     */
    public fun protocol(protocol: Protocol?): Request {
        this.protocol = protocol
        return this
    }

    /**
     * Define the HTTP version as a hint to the application layer.
     */
    public fun httpVersion(version: HttpVersion?): Request {
        this.httpVersion = version
        return this
    }

    /**
     * Convert this request into a [TransportContext].
     */
    public fun toTransportContext(): TransportContext =
        TransportContext(
            protocol = TransportProtocol.Tcp,
            appProtocol = protocol,
            httpVersion = httpVersion,
            authority = authority,
        )

    /**
     * Try to extract transport context from this request reference.
     */
    public fun tryRefIntoTransportContext(): Result<TransportContext> =
        Result.success(toTransportContext())

    /**
     * Try to extract transport context from this request reference.
     */
    public fun tryRefIntoTransportCtx(): Result<TransportContext> =
        Result.success(toTransportContext())

    public companion object {
        /**
         * Create a new TCP Request with default Extensions.
         */
        public fun new(authority: HostWithPort): Request =
            Request(authority = authority)

        /**
         * Create a new TCP Request with given Extensions.
         */
        public fun newWithExtensions(authority: HostWithPort, extensions: Extensions): Request =
            Request(authority = authority, extensions = extensions)

        /**
         * Convert a [TransportContext] into a [Request].
         */
        public fun from(context: TransportContext): Request =
            Request(
                authority = context.authority,
                protocol = context.appProtocol,
                httpVersion = context.httpVersion,
            )
    }
}
