// port-lint: source client/request.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramatcp.client

import io.github.kotlinmania.http.Version
import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.ExtensionsRef
import io.github.kotlinmania.ramanet.NetProtocol
import io.github.kotlinmania.ramanet.address.HostWithOptPort
import io.github.kotlinmania.ramanet.address.HostWithPort
import io.github.kotlinmania.ramanet.transport.TransportContext
import io.github.kotlinmania.ramanet.transport.TransportProtocol
import io.github.kotlinmania.ramanet.transport.TryRefIntoTransportContext
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * HTTP version type alias matching upstream `rama_http_types::Version` (`http::Version`).
 */
public typealias HttpVersion = Version

/**
 * A request to establish a TCP Connection.
 *
 * This can be used when operating on a layer below an application layer such as HTTP.
 */
public data class Request(
    public var authority: HostWithPort,
    public var protocol: NetProtocol? = null,
    public var httpVersion: Version? = null,
    private var extensionsStore: Extensions = Extensions.new(),
) : ExtensionsRef,
    ExtensionsMut {
    override fun extensions(): Extensions = extensionsStore

    override fun extensionsMut(): Extensions = extensionsStore

    /**
     * Define the application protocol for this request.
     */
    public fun protocol(protocol: NetProtocol?): Request {
        this.protocol = protocol
        return this
    }

    /**
     * Set the application protocol for this request.
     */
    public fun setProtocol(protocol: NetProtocol?): Request {
        this.protocol = protocol
        return this
    }

    /**
     * Return a copy of this request with the specified application protocol.
     */
    public fun withProtocol(protocol: NetProtocol?): Request {
        val copy = this.copy(extensionsStore = extensionsStore.copy())
        copy.protocol = protocol
        return copy
    }

    /**
     * Define the HTTP version as a hint to the application layer.
     */
    public fun httpVersion(version: Version?): Request {
        this.httpVersion = version
        return this
    }

    /**
     * Set the HTTP version as a hint to the application layer.
     */
    public fun setHttpVersion(version: Version?): Request {
        this.httpVersion = version
        return this
    }

    /**
     * Return a copy of this request with the specified HTTP version.
     */
    public fun withHttpVersion(version: Version?): Request {
        val copy = this.copy(extensionsStore = extensionsStore.copy())
        copy.httpVersion = version
        return copy
    }

    /**
     * Set the authority for this request.
     */
    public fun setAuthority(authority: HostWithPort): Request {
        this.authority = authority
        return this
    }

    /**
     * Return a copy of this request with the specified authority.
     */
    public fun withAuthority(authority: HostWithPort): Request {
        val copy = this.copy(extensionsStore = extensionsStore.copy())
        copy.authority = authority
        return copy
    }

    /**
     * Return a copy of this request with the specified extensions.
     */
    public fun withExtensions(extensions: Extensions): Request =
        this.copy(extensionsStore = extensions.copy())

    /**
     * Convert this request into a [TransportContext].
     */
    public fun toTransportContext(): TransportContext =
        TransportContext(
            protocol = TransportProtocol.Tcp,
            authority = HostWithOptPort(authority.host, authority.port),
            appProtocol = protocol,
            httpVersion = httpVersion?.fmt(),
        )

    /**
     * Try to extract transport context from this request reference.
     */
    @HiddenFromObjC
    public fun tryRefIntoTransportContext(): Result<TransportContext> =
        Result.success(toTransportContext())

    /**
     * Try to extract transport context from this request reference.
     */
    @HiddenFromObjC
    public fun tryRefIntoTransportCtx(): Result<TransportContext> =
        Result.success(toTransportContext())

    /**
     * View this request as a [TryRefIntoTransportContext].
     */
    @HiddenFromObjC
    public fun asTransportContextRef(): TryRefIntoTransportContext =
        TryRefIntoTransportContext { Result.success(toTransportContext()) }

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
        public fun from(context: TransportContext): Request {
            val hostWithPort =
                context.hostWithPort()
                    ?: HostWithPort(context.authority.host, context.authority.port ?: NetProtocol.HTTP_DEFAULT_PORT)
            return Request(
                authority = hostWithPort,
                protocol = context.appProtocol,
                httpVersion = parseHttpVersion(context.httpVersion),
            )
        }

        private fun parseHttpVersion(wireName: String?): Version? =
            when (wireName) {
                "HTTP/0.9" -> Version.HTTP_09
                "HTTP/1.0" -> Version.HTTP_10
                "HTTP/1.1" -> Version.HTTP_11
                "HTTP/2.0", "H2" -> Version.HTTP_2
                "HTTP/3.0", "H3" -> Version.HTTP_3
                else -> null
            }

        /**
         * Error type for infallible request conversions.
         */
        public typealias Error = Nothing
    }
}

/**
 * Error type for infallible request conversions.
 */
public typealias RequestError = Nothing
