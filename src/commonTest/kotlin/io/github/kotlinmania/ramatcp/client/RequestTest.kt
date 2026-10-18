// port-lint: tests client/request.rs
package io.github.kotlinmania.ramatcp.client

import io.github.kotlinmania.http.Version
import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramanet.NetProtocol
import io.github.kotlinmania.ramanet.TransportProtocol
import io.github.kotlinmania.ramanet.address.HostWithOptPort
import io.github.kotlinmania.ramanet.address.HostWithPort
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RequestTest {
    @Test
    fun testRequestCreationAndMutation() {
        val authority = HostWithPort.exampleDomainHttps()
        val req = Request.new(authority)

        assertEquals(authority, req.authority)
        assertNull(req.protocol)
        assertNull(req.httpVersion)

        req.protocol(NetProtocol.HTTPS)
        assertEquals(NetProtocol.HTTPS, req.protocol)

        req.httpVersion(Version.HTTP_2)
        assertEquals(Version.HTTP_2, req.httpVersion)

        val ext = Extensions.new()
        ext.insert("custom-token")
        val reqWithExt = Request.newWithExtensions(authority, ext)
        assertEquals("custom-token", reqWithExt.extensions().get<String>())
    }

    @Test
    fun testTransportContextConversion() {
        val authority = HostWithPort.exampleDomainHttps()
        val req = Request.new(authority).protocol(NetProtocol.HTTPS).httpVersion(Version.HTTP_2)

        val ctx = req.toTransportContext()
        assertEquals(TransportProtocol.Tcp, ctx.protocol)
        assertEquals(NetProtocol.HTTPS, ctx.appProtocol)
        assertEquals("HTTP/2.0", ctx.httpVersion)
        assertEquals(HostWithOptPort(authority.host, authority.port), ctx.authority)

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
        val auth1 = HostWithPort.exampleDomainHttp()
        val auth2 = HostWithPort.parse("api.example.com:443")
        val req = Request.new(auth1)

        req.setProtocol(NetProtocol.HTTP)
        assertEquals(NetProtocol.HTTP, req.protocol)

        req.setHttpVersion(Version.HTTP_11)
        assertEquals(Version.HTTP_11, req.httpVersion)

        req.setAuthority(auth2)
        assertEquals(auth2, req.authority)

        val withProt = req.withProtocol(NetProtocol.HTTPS)
        assertEquals(NetProtocol.HTTPS, withProt.protocol)
        assertEquals(NetProtocol.HTTP, req.protocol)

        val withVer = req.withHttpVersion(Version.HTTP_2)
        assertEquals(Version.HTTP_2, withVer.httpVersion)
        assertEquals(Version.HTTP_11, req.httpVersion)

        val withAuth = req.withAuthority(auth1)
        assertEquals(auth1, withAuth.authority)
        assertEquals(auth2, req.authority)

        val ext = Extensions.new()
        ext.insert(42)
        val withExt = req.withExtensions(ext)
        assertEquals(42, withExt.extensions().get<Int>())
    }
}
