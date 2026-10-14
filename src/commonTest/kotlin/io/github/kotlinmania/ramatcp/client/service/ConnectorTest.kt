// port-lint: tests client/service/connector.rs
package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramanet.address.HostWithPort
import io.github.kotlinmania.ramanet.address.ProxyAddress
import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.client.ConnectorTarget
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
        val req = Request.new(HostWithPort.localIpv4(8080u))
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

        val req = Request.new(HostWithPort.localIpv4(9090u))
        val established = runSync { connector.connect(req) }

        assertNotNull(established.conn)
        assertEquals(SocketAddress.localIpv4(44444u), established.conn.localAddr())
    }

    @Test
    fun testTcpConnectorWithProxyAndTarget() {
        val connector = TcpConnector.default()

        val reqProxy = Request.new(HostWithPort.parse("target.internal:80"))
        reqProxy.extensions().insert(ProxyAddress(HostWithPort.localIpv4(8888u)))
        val establishedProxy = runSync { connector.serve(reqProxy) }
        assertNotNull(establishedProxy.conn)

        val reqTarget = Request.new(HostWithPort.parse("target.internal:80"))
        reqTarget.extensions().insert(ConnectorTarget(HostWithPort.localIpv4(9999u)))
        val establishedTarget = runSync { connector.serve(reqTarget) }
        assertNotNull(establishedTarget.conn)
    }
}
