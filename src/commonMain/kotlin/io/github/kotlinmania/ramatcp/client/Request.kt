// port-lint: source client/request.rs
package io.github.kotlinmania.ramatcp.client

import io.github.kotlinmania.ramatcp.Extensions
import io.github.kotlinmania.ramatcp.ExtensionsMut
import io.github.kotlinmania.ramatcp.ExtensionsRef
import io.github.kotlinmania.ramatcp.HostWithPort

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
    }
}
