// port-lint: tests client/service/forward.rs
package io.github.kotlinmania.ramatcp.client.service

import io.github.kotlinmania.ramatcp.HostWithPort
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.ramatcp.runSync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ForwardTest {
    @Test
    fun testStaticAndDynamicForwarder() {
        val target = HostWithPort("example.com", 80u)
        val staticFwd = Forwarder.new(target)
        val staticKind = staticFwd.kind
        assertTrue(staticKind is ForwarderKind.Static)
        assertEquals(target, staticKind.target)

        val dynFwd = Forwarder.ctx()
        assertTrue(dynFwd.kind is ForwarderKind.Dynamic)

        val customFwd = staticFwd.withConnector("dummyConnector")
        assertEquals("dummyConnector", customFwd.connector)
    }

    @Test
    fun testForwarderServe() {
        val target = HostWithPort("127.0.0.1", 8080u)
        val fwd = Forwarder.new(target)
        val stream = TcpStream.new()
        runSync {
            fwd.serve(stream)
        }

        val dynFwd = Forwarder.ctx()
        val dynStream = TcpStream.new()
        dynStream.extensions().insert(ProxyTarget(target))
        runSync {
            dynFwd.serve(dynStream)
        }
    }
}
