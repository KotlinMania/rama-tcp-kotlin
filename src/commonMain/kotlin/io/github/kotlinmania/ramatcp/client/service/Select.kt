// port-lint: source client/service/select.rs
package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramatcp.client.TcpStreamConnector

/**
 * Contains a Connector created by a TcpStreamConnectorFactory.
 */
public data class CreatedTcpStreamConnector<Connector : TcpStreamConnector>(
    public val connector: Connector,
)

/**
 * Factory to create a TcpStreamConnector.
 */
public interface TcpStreamConnectorFactory<Connector : TcpStreamConnector> {
    /**
     * Try to create a TcpStreamConnector.
     */
    public suspend fun makeConnector(): CreatedTcpStreamConnector<Connector>
}

/**
 * Utility implementation of a TcpStreamConnectorFactory that returns a pre-existing connector.
 */
public class TcpStreamConnectorCloneFactory<Connector : TcpStreamConnector>(
    private val connector: Connector,
) : TcpStreamConnectorFactory<Connector> {
    override suspend fun makeConnector(): CreatedTcpStreamConnector<Connector> =
        CreatedTcpStreamConnector(connector)
}
