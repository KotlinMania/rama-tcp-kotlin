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

        req.protocol(AppProtocol.Https)
        assertEquals(AppProtocol.Https, req.protocol)

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
        val req = Request.new(authority).protocol(AppProtocol.Https).httpVersion(HttpVersion.H2)

        val ctx = req.toTransportContext()
        assertEquals(TransportProtocol.Tcp, ctx.protocol)
        assertEquals(AppProtocol.Https, ctx.appProtocol)
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

    @Test
    fun testSetAndWithMethods() {
        val auth1 = HostWithPort("example.com", 80u)
        val auth2 = HostWithPort("api.example.com", 443u)
        val req = Request.new(auth1)

        req.setProtocol(AppProtocol.Http)
        assertEquals(AppProtocol.Http, req.protocol)

        req.setHttpVersion(HttpVersion.Http11)
        assertEquals(HttpVersion.Http11, req.httpVersion)

        req.setAuthority(auth2)
        assertEquals(auth2, req.authority)

        val withProt = req.withProtocol(AppProtocol.Https)
        assertEquals(AppProtocol.Https, withProt.protocol)
        assertEquals(AppProtocol.Http, req.protocol)

        val withVer = req.withHttpVersion(HttpVersion.H2)
        assertEquals(HttpVersion.H2, withVer.httpVersion)
        assertEquals(HttpVersion.Http11, req.httpVersion)

        val withAuth = req.withAuthority(auth1)
        assertEquals(auth1, withAuth.authority)
        assertEquals(auth2, req.authority)

        val ext = Extensions()
        ext.insert(42)
        val withExt = req.withExtensions(ext)
        assertEquals(42, withExt.extensions().get<Int>())
    }
}
