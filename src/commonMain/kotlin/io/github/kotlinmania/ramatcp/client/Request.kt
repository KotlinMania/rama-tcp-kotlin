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
    public val appProtocol: AppProtocol? = null,
    public val httpVersion: HttpVersion? = null,
    public val authority: HostWithPort,
)

/**
 * Protocol identifier for application layer hint.
 */
public enum class AppProtocol {
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
    public var protocol: AppProtocol? = null,
    public var httpVersion: HttpVersion? = null,
    private var extensionsStore: Extensions = Extensions(),
) : ExtensionsRef,
    ExtensionsMut {
    override fun extensions(): Extensions = extensionsStore

    override fun extensionsMut(): Extensions = extensionsStore

    /**
     * Define the application protocol for this request.
     */
    public fun protocol(protocol: AppProtocol?): Request {
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
            Request(authority = authority, extensionsStore = extensions)

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
