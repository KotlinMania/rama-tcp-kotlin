// port-lint: tests rama-tcp/src/pool/mod.rs
package io.github.kotlinmania.ramatcp.pool

import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.client.TcpStreamConnector
import io.github.kotlinmania.ramatcp.runSync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ModTest {
    private class MockConnector(
        val address: SocketAddress,
    ) : TcpStreamConnector {
        override suspend fun connect(addr: SocketAddress): TcpStream =
            TcpStream.withAddresses(localAddress = address, peerAddress = addr)
    }

    @Test
    fun testSelectorRoundRobin() {
        val connectors =
            listOf(
                SocketAddress.localIpv4(8080u),
                SocketAddress.localIpv4(8081u),
                SocketAddress.localIpv4(8082u),
                SocketAddress.localIpv4(8083u),
                SocketAddress.localIpv4(8084u),
            )

        val numberOfSample = connectors.size * 2
        val selector = Selector.newRoundRobin()

        val expected = (0 until numberOfSample).map { connectors[it % connectors.size] }
        val results =
            (0 until numberOfSample).map {
                val next = selector.next(connectors)
                assertNotNull(next, "Selector could not select from empty Connections collection")
                next
            }

        assertEquals(expected, results, "Selector returned unexpected order")
    }

    @Test
    fun testSelectorRandom() {
        val connectors =
            listOf(
                SocketAddress.localIpv4(8080u),
                SocketAddress.localIpv4(8081u),
                SocketAddress.localIpv4(8082u),
            )

        val numberOfSample = connectors.size * 2
        val selector = Selector.newRandom()

        val results =
            (0 until numberOfSample).map {
                selector.next(connectors)
            }

        assertTrue(results.all { it != null }, "Unexpected got null from selector")
    }

    @Test
    fun testSelectorRandomSelectionCoverage() {
        val connectors =
            listOf(
                SocketAddress.localIpv4(8080u),
                SocketAddress.localIpv4(8081u),
                SocketAddress.localIpv4(8082u),
            )

        val selector = Selector.newRandom()
        val seen = mutableSetOf<SocketAddress>()

        for (i in 0 until 1000) {
            selector.next(connectors)?.let { seen.add(it) }
            if (seen.size == connectors.size) {
                break
            }
        }

        assertEquals(
            connectors.size,
            seen.size,
            "Random selector should be able to select all available connectors",
        )
    }

    @Test
    fun testEmptySelectors() {
        val connectors: List<SocketAddress> = emptyList()
        val selectors = listOf(Selector.newRoundRobin(), Selector.newRandom())
        for (selector in selectors) {
            val next = selector.next(connectors)
            assertNull(next, "Empty selector should return null")
        }
    }

    @Test
    fun testErrorReturnedFromEmptyTcpStreamConnectorPool() {
        val connectors: List<MockConnector> = emptyList()
        val randomConnectorPool = TcpStreamConnectorPool.newRoundRobin(connectors)
        assertFailsWith<PoolError> {
            runSync {
                randomConnectorPool.connect(SocketAddress.parse("127.0.0.1:8080"))
            }
        }
    }
}
