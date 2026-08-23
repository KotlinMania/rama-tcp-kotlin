// port-lint: tests client/request.rs
package io.github.kotlinmania.ramatcp.client

import io.github.kotlinmania.ramatcp.Extensions
import io.github.kotlinmania.ramatcp.HostWithPort
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RequestTest {
    @Test
    fun testRequestCreationAndMutation() {
        val authority = HostWithPort("example.com", 443u)
        val req = Request.new(authority)

        assertEquals(authority, req.authority)
        assertNull(req.protocol)
        assertNull(req.httpVersion)

        req.protocol(Protocol.Https)
        assertEquals(Protocol.Https, req.protocol)

        req.httpVersion(HttpVersion.H2)
        assertEquals(HttpVersion.H2, req.httpVersion)

        val ext = Extensions()
        ext.insert("custom-token")
        val reqWithExt = Request.newWithExtensions(authority, ext)
        assertEquals("custom-token", reqWithExt.extensions().get<String>())
    }

    @Test
    fun testTransportContextConversion() {
        val authority = HostWithPort("example.com", 443u)
        val req = Request.new(authority).protocol(Protocol.Https).httpVersion(HttpVersion.H2)

        val ctx = req.toTransportContext()
        assertEquals(TransportProtocol.Tcp, ctx.protocol)
        assertEquals(Protocol.Https, ctx.appProtocol)
        assertEquals(HttpVersion.H2, ctx.httpVersion)
        assertEquals(authority, ctx.authority)

        val res = req.tryRefIntoTransportContext()
        assertTrue(res.isSuccess)
        assertEquals(ctx, res.getOrNull())

        val fromCtx = Request.from(ctx)
        assertEquals(req.authority, fromCtx.authority)
        assertEquals(req.protocol, fromCtx.protocol)
        assertEquals(req.httpVersion, fromCtx.httpVersion)
    }
}
