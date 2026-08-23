// port-lint: tests client/service/connector.rs
package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramatcp.HostWithPort
import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.client.DefaultDnsResolver
import io.github.kotlinmania.ramatcp.client.FunctionTcpStreamConnector
import io.github.kotlinmania.ramatcp.client.Request
import io.github.kotlinmania.ramatcp.runSync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ConnectorTest {
    @Test
    fun testTcpConnectorConnect() {
        val connector = TcpConnector.new()
        val req = Request.new(HostWithPort("127.0.0.1", 8080u))
        val established = runSync { connector.connect(req) }

        assertNotNull(established.conn)
        assertEquals(req, established.input)
    }

    @Test
    fun testTcpConnectorWithCustomDnsAndConnector() {
        val customDns = DefaultDnsResolver()
        val customConnector =
            FunctionTcpStreamConnector { addr ->
                TcpStream.withAddresses(
                    localAddress = SocketAddress.localIpv4(44444u),
                    peerAddress = addr,
                )
            }

        val connector =
            TcpConnector
                .new()
                .withDns(customDns)
                .withConnector(customConnector)

        val req = Request.new(HostWithPort("127.0.0.1", 9090u))
        val established = runSync { connector.connect(req) }

        assertNotNull(established.conn)
        assertEquals(SocketAddress.localIpv4(44444u), established.conn.localAddr())
    }

    @Test
    fun testTcpConnectorWithProxyAndTarget() {
        val connector = TcpConnector.default()

        val reqProxy = Request.new(HostWithPort("target.internal", 80u))
        reqProxy.extensions.insert(ProxyAddress(HostWithPort("127.0.0.1", 8888u)))
        val establishedProxy = runSync { connector.serve(reqProxy) }
        assertNotNull(establishedProxy.conn)

        val reqTarget = Request.new(HostWithPort("target.internal", 80u))
        reqTarget.extensions.insert(ConnectorTarget(HostWithPort("127.0.0.1", 9999u)))
        val establishedTarget = runSync { connector.serve(reqTarget) }
        assertNotNull(establishedTarget.conn)
    }
}
