// port-lint: tests client/connect.rs
package io.github.kotlinmania.ramatcp.client

import io.github.kotlinmania.ramatcp.Extensions
import io.github.kotlinmania.ramatcp.HostWithPort
import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.runSync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ConnectTest {
    @Test
    fun testDefaultTcpConnect() {
        val ext = Extensions()
        val address = HostWithPort("127.0.0.1", 8080u)
        val (stream, socketAddr) = runSync { defaultTcpConnect(ext, address) }

        assertNotNull(stream)
        assertEquals("127.0.0.1", socketAddr.ip)
        assertEquals(8080u.toUShort(), socketAddr.port)
    }

    @Test
    fun testTcpConnectWithCustomConnector() {
        val ext = Extensions()
        val address = HostWithPort("localhost", 9000u)
        val connector =
            FunctionTcpStreamConnector { addr ->
                TcpStream.withAddresses(
                    localAddress = SocketAddress.localIpv4(50000u),
                    peerAddress = addr,
                )
            }

        val (stream, socketAddr) =
            runSync {
                tcpConnect(
                    extensions = ext,
                    address = address,
                    dns = DefaultDnsResolver(),
                    connector = connector,
                )
            }

        assertNotNull(stream)
        assertEquals(9000u.toUShort(), socketAddr.port)
        assertEquals(SocketAddress.localIpv4(50000u), stream.localAddr())
    }
}
