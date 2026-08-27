// port-lint: source client/service/select.rs
package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramatcp.client.DefaultTcpStreamConnector
import io.github.kotlinmania.ramatcp.client.TcpStreamConnector

/**
 * Contains a Connector created by a [TcpStreamConnectorFactory],
 * together with the context used to create it in relation to.
 */
public data class CreatedTcpStreamConnector<out Connector : TcpStreamConnector>(
    public val connector: Connector,
)

/**
 * Factory to create a [TcpStreamConnector]. This is used by the TCP
 * stream service to create a stream within a specific context.
 *
 * In the most simplest case you use a [TcpStreamConnectorCloneFactory]
 * to use a cloneable connector, but in more advanced cases you can use variants
 * of [TcpStreamConnector] specific to the given contexts.
 */
public interface TcpStreamConnectorFactory<Connector : TcpStreamConnector> {
    /**
     * Try to create a [TcpStreamConnector], and return the created connector.
     */
    public suspend fun makeConnector(): CreatedTcpStreamConnector<Connector>
}

/**
 * Unit implementation of [TcpStreamConnectorFactory] producing a default connector.
 */
public class UnitTcpStreamConnectorFactory : TcpStreamConnectorFactory<DefaultTcpStreamConnector> {
    override suspend fun makeConnector(): CreatedTcpStreamConnector<DefaultTcpStreamConnector> =
        CreatedTcpStreamConnector(DefaultTcpStreamConnector())
}

/**
 * Utility implementation of a [TcpStreamConnectorFactory] which is implemented
 * to allow one to use a connector as a [TcpStreamConnectorFactory].
 */
public class TcpStreamConnectorCloneFactory<Connector : TcpStreamConnector>(
    public val connector: Connector,
) : TcpStreamConnectorFactory<Connector> {
    override suspend fun makeConnector(): CreatedTcpStreamConnector<Connector> =
        CreatedTcpStreamConnector(connector)

    public companion object {
        public typealias Connector = FactoryConnector
        public typealias Error = SelectError
    }
}

/**
 * Error type alias for connector factory operations.
 */
public typealias SelectError = Exception

/**
 * Connector type alias for factory definitions.
 */
public typealias FactoryConnector = TcpStreamConnector




