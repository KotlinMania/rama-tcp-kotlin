// port-lint: source pool/mod.rs
package io.github.kotlinmania.ramatcp.pool

import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.client.TcpStreamConnector
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.random.Random

/**
 * Selection algorithms for connector pooling.
 */
@OptIn(ExperimentalAtomicApi::class)
public sealed class Selector {
    /**
     * Round-robin connector selector with atomic counter.
     */
    public class RoundRobin(
        private val counter: AtomicInt = AtomicInt(0),
    ) : Selector() {
        override fun <C> next(connectors: List<C>): C? {
            if (connectors.isEmpty()) return null
            val count = counter.fetchAndAdd(1)
            val positiveIndex = (count and 0x7FFFFFFF) % connectors.size
            return connectors[positiveIndex]
        }
    }

    /**
     * Random connector selector.
     */
    public data object RandomSelector : Selector() {
        override fun <C> next(connectors: List<C>): C? {
            if (connectors.isEmpty()) return null
            val idx = Random.nextInt(connectors.size)
            return connectors[idx]
        }
    }

    /**
     * Select the next connector from the given list.
     */
    public abstract fun <C> next(connectors: List<C>): C?

    public companion object {
        /** Create a new random selector. */
        public fun newRandom(): Selector = RandomSelector

        /** Create a new round-robin selector. */
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
                ?: throw IllegalStateException("TcpStreamConnectorPool has empty connectors collection")
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
