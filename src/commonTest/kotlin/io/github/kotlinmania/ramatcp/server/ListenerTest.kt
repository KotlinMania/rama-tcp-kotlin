// port-lint: tests server/listener.rs
package io.github.kotlinmania.ramatcp.server

import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.socket.Interface
import io.github.kotlinmania.ramanet.stream.SocketInfo
import io.github.kotlinmania.ramatcp.runSync
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ListenerTest {
    @Test
    fun testTcpListenerBuilderAndBind() {
        val builder = TcpListener.build().ttl(64u)
        assertEquals(64u, builder.ttl)

        val addr = SocketAddress.localIpv4(8080u)
        val listener = runSync { builder.bindAddress(addr) }

        assertEquals(addr, listener.localAddr())
        assertEquals(64u, listener.ttl())

        val (stream, peer) = runSync { listener.accept() }
        assertNotNull(stream)
        assertNotNull(peer)
        assertEquals(addr, stream.localAddr())
    }

    @Test
    fun testTcpListenerBindStringAndInterface() {
        val listener = runSync { TcpListener.bind("127.0.0.1:9090") }
        assertEquals(SocketAddress.parse("127.0.0.1:9090"), listener.localAddr())

        val ifaceListener = runSync { TcpListener.bind(Interface.localIpv4(9092u)) }
        assertEquals(SocketAddress.localIpv4(9092u), ifaceListener.localAddr())
    }

    @Test
    fun testTcpListenerServe() {
        val listener = runSync { TcpListener.bind("127.0.0.1:9091") }
        var served = false
        runSync {
            listener.serve { stream ->
                assertNotNull(stream)
                assertNotNull(stream.extensions().get<SocketInfo>())
                served = true
            }
        }
        assertEquals(true, served)
    }
}
