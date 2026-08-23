// port-lint: source server/mod.rs
package io.github.kotlinmania.ramatcp.server

/**
 * TCP server module for Rama.
 *
 * The TCP server is used to create a [TcpListener] and accept incoming connections.
 *
 * Example:
 * ```kotlin
 * val listener = TcpListener.bind("127.0.0.1:9000")
 * listener.serve { stream ->
 *     val resp = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nHello"
 *     // write to stream
 * }
 * ```
 */
public object ServerModule {
    /** Marker constant for the server module. */
    public const val NAME: String = "server"
}
