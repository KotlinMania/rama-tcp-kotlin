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

    @Test
    fun testSocketAddressConnectorAndDnsOverwrite() {
        val ext = Extensions()
        ext.insert(DnsOverwrite(mapOf("my.service" to listOf("10.0.0.1"))))

        val bindAddr = SocketAddress.localIpv4(33333u)
        val connector = SocketAddressConnector(bindAddr)
        val (stream, targetAddr) =
            runSync {
                tcpConnect(
                    extensions = ext,
                    address = HostWithPort("my.service", 80u),
                    connector = connector,
                )
            }

        assertEquals("10.0.0.1", targetAddr.ip)
        assertEquals(80u.toUShort(), targetAddr.port)
        assertEquals(bindAddr, stream.localAddr())

        val branchStream =
            runSync {
                tcpConnectInnerBranch(
                    extensions = ext,
                    domain = "localhost",
                    port = 80u,
                    dns = DefaultDnsResolver(),
                    connector = connector,
                    ipKind = IpKind.V4,
                )
            }
        assertEquals(bindAddr, branchStream.first.localAddr())

        val optsStream =
            runSync {
                tcpConnectWithSocketOpts(null, SocketAddress.localIpv4(80u))
            }
        assertNotNull(optsStream)
    }
}
