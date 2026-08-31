// port-lint: source pool/mod.rs
package io.github.kotlinmania.ramatcp.pool

import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.client.TcpStreamConnector
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.random.Random

/**
 * Pool error type.
 */
public class PoolError(
    message: String,
) : Exception(message)

/**
 * Selection algorithms
 */
@OptIn(ExperimentalAtomicApi::class)
public sealed class Selector {
    /**
     * Round-robin selection using atomic counter.
     */
    public class RoundRobin(
        public val atomicUsize: AtomicInt = AtomicInt(0),
    ) : Selector()

    /**
     * Random selection.
     */
    public data object Random : Selector()

    /**
     * Select the next connector from the given list.
     */
    public fun <C> next(connectors: List<C>): C? {
        if (connectors.isEmpty()) {
            return null
        }
        val selection =
            when (this) {
                is RoundRobin -> (atomicUsize.fetchAndAdd(1) and 0x7FFFFFFF)
                is Random -> kotlin.random.Random.nextInt(Int.MAX_VALUE)
            }
        val idx = selection % connectors.size
        return connectors[idx]
    }

    public companion object {
        public fun newRandom(): Selector = Random

        public fun newRoundRobin(): Selector = RoundRobin()
    }
}

/**
 * A pool of TCP stream connectors with configurable selection algorithm.
 */
public class TcpStreamConnectorPool<C : TcpStreamConnector>(
    public val selector: Selector,
    public val connectors: List<C>,
) : TcpStreamConnector {
    override suspend fun connect(addr: SocketAddress): TcpStream {
        val connector =
            selector.next(connectors)
                ?: throw PoolError("cannot select connector from empty collection")
        return connector.connect(addr)
    }

    public companion object {
        /**
         * Create a pool where each connection is chosen randomly.
         */
        public fun <C : TcpStreamConnector> newRandom(connectors: List<C>): TcpStreamConnectorPool<C> =
            TcpStreamConnectorPool(
                selector = Selector.newRandom(),
                connectors = connectors,
            )

        /**
         * Create a pool where each connection is chosen using round-robin.
         */
        public fun <C : TcpStreamConnector> newRoundRobin(connectors: List<C>): TcpStreamConnectorPool<C> =
            TcpStreamConnectorPool(
                selector = Selector.newRoundRobin(),
                connectors = connectors,
            )
    }
}

/**
 * Pool error type alias matching trait Error associated type.
 */
public typealias Error = PoolError
