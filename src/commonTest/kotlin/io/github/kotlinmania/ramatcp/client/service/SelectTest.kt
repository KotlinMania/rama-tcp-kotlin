// port-lint: tests client/service/select.rs
package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.client.DefaultTcpStreamConnector
import io.github.kotlinmania.ramatcp.client.FunctionTcpStreamConnector
import io.github.kotlinmania.ramatcp.runSync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SelectTest {
    @Test
    fun testUnitTcpStreamConnectorFactory() {
        val factory = UnitTcpStreamConnectorFactory()
        val created = runSync { factory.makeConnector() }
        assertNotNull(created.connector)

        val stream = runSync { created.connector.connect(SocketAddress.localIpv4(8080u)) }
        assertNotNull(stream)
    }

    @Test
    fun testTcpStreamConnectorCloneFactory() {
        val connector = FunctionTcpStreamConnector { addr ->
            TcpStream.withAddresses(
                localAddress = SocketAddress.localIpv4(55555u),
                peerAddress = addr,
            )
        }
        val factory = TcpStreamConnectorCloneFactory(connector)
        val created = runSync { factory.makeConnector() }
        val stream = runSync { created.connector.connect(SocketAddress.localIpv4(80u)) }
        assertEquals(SocketAddress.localIpv4(55555u), stream.localAddr())
        assertEquals(SocketAddress.localIpv4(80u), stream.peerAddr())
    }
}
