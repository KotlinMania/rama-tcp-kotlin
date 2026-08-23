// port-lint: source server/mod.rs
package io.github.kotlinmania.ramatcp.server

/**
 * TCP server module for Rama.
 *
 * The TCP server is used to create a TcpListener and accept incoming connections.
 */
public object ServerModule {
    /** Marker constant for the server module. */
    public const val NAME: String = "server"
}
