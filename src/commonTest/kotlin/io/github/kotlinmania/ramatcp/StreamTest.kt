// port-lint: tests stream.rs
package io.github.kotlinmania.ramatcp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StreamTest {
    @Test
    fun testExtensionsCrud() {
        val ext = Extensions()
        assertTrue(ext.isEmpty())
        assertEquals(0, ext.size)

        ext.insert("hello")
        ext.insert(42)

        assertTrue(ext.contains<String>())
        assertTrue(ext.contains<Int>())
        assertFalse(ext.contains<Double>())

        assertEquals("hello", ext.get<String>())
        assertEquals(42, ext.get<Int>())
        assertNull(ext.get<Double>())

        val copied = ext.copy()
        assertEquals(2, copied.size)
        assertTrue(copied.contains<String>())

        assertTrue(ext.remove<String>())
        assertFalse(ext.contains<String>())
        assertNull(ext.get<String>())

        ext.clear()
        assertTrue(ext.isEmpty())
    }

    @Test
    fun testSocketAddress() {
        val ipv4 = SocketAddress.localIpv4(8080u)
        assertEquals("127.0.0.1", ipv4.ip)
        assertEquals(8080u.toUShort(), ipv4.port)
        assertFalse(ipv4.isIpv6())
        assertEquals("127.0.0.1:8080", ipv4.toString())

        val ipv6 = SocketAddress.localIpv6(9090u)
        assertTrue(ipv6.isIpv6())
        assertEquals("[::1]:9090", ipv6.toString())

        val parsed = SocketAddress.parse("192.168.1.1:3000")
        assertEquals("192.168.1.1", parsed.ip)
        assertEquals(3000u.toUShort(), parsed.port)

        val parsedV6 = SocketAddress.parse("[::1]:8080")
        assertEquals("::1", parsedV6.ip)
        assertEquals(8080u.toUShort(), parsedV6.port)
    }

    @Test
    fun testTcpStream() {
        val local = SocketAddress.localIpv4(12345u)
        val peer = SocketAddress.localIpv4(8080u)
        val stream =
            TcpStream.withAddresses(
                localAddress = local,
                peerAddress = peer,
            )

        assertNotNull(stream.extensions)
        assertEquals(local, stream.localAddr())
        assertEquals(peer, stream.peerAddr())

        assertEquals(5, stream.pollRead(ByteArray(5)))
        assertEquals(5, stream.pollWrite(ByteArray(5)))
        assertEquals(10, stream.pollWriteVectored(listOf(ByteArray(5), ByteArray(5))))
        assertTrue(stream.pollFlush())
        assertTrue(stream.pollShutdown())
        assertFalse(stream.isWriteVectored())
        assertNull(stream.toTokioTcpStream())
    }
}
