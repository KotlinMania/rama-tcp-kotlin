// port-lint: source client/service/mod.rs
package io.github.kotlinmania.ramatcp.client.service

/**
 * TCP services module for Rama.
 *
 * Provides [Forwarder], [DefaultForwarder], [TcpConnector],
 * [CreatedTcpStreamConnector], [TcpStreamConnectorCloneFactory], and [TcpStreamConnectorFactory].
 */
public object ServiceModule {
    /** Marker constant for the client service module. */
    public const val NAME: String = "service"
}
