// port-lint: source client/mod.rs
package io.github.kotlinmania.ramatcp.client

/**
 * Rama TCP Client module definitions and re-exports.
 *
 * Provides [TcpStreamConnector], [defaultTcpConnect], [tcpConnect], and [Request].
 */
public object ClientModule {
    /** Marker constant for the client module. */
    public const val NAME: String = "client"
}
