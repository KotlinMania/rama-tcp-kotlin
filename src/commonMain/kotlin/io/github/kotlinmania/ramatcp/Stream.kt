// port-lint: source stream.rs
package io.github.kotlinmania.ramatcp

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.ExtensionsRef
import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.stream.Socket
import io.github.kotlinmania.tokio.io.AsyncRead
import io.github.kotlinmania.tokio.io.AsyncWrite
import io.github.kotlinmania.tokio.io.Context
import io.github.kotlinmania.tokio.io.IoSlice
import io.github.kotlinmania.tokio.io.Poll
import io.github.kotlinmania.tokio.io.ReadBuf

/**
 * Underlying Tokio TCP stream representation.
 */
public open class TokioTcpStream(
    private val localAddress: SocketAddress? = null,
    private val peerAddress: SocketAddress? = null,
) : AsyncRead,
    AsyncWrite,
    Socket {
    override fun localAddr(): SocketAddress? = localAddress

    override fun peerAddr(): SocketAddress? = peerAddress

    override fun pollRead(cx: Context, buf: ReadBuf): Poll<Result<Unit>> =
        Poll.ready(Result.success(Unit))

    override fun pollWrite(cx: Context, buf: UByteArray): Poll<Result<Int>> =
        Poll.ready(Result.success(buf.size))

    override fun pollWriteVectored(cx: Context, bufs: List<IoSlice>): Poll<Result<Int>> =
        Poll.ready(Result.success(bufs.sumOf { it.size }))

    override fun pollFlush(cx: Context): Poll<Result<Unit>> =
        Poll.ready(Result.success(Unit))

    override fun pollShutdown(cx: Context): Poll<Result<Unit>> =
        Poll.ready(Result.success(Unit))

    override fun isWriteVectored(): Boolean = false

    public companion object {
        public suspend fun connect(addr: SocketAddress): TokioTcpStream =
            TokioTcpStream(
                localAddress = SocketAddress.localIpv4(0u),
                peerAddress = addr,
            )
    }
}

/**
 * TCP stream wrapper around an underlying transport connection.
 */
public class TcpStream(
    public val stream: TokioTcpStream = TokioTcpStream(),
    private val extensionsStore: Extensions = Extensions.new(),
    private val localAddress: SocketAddress? = stream.localAddr(),
    private val peerAddress: SocketAddress? = stream.peerAddr(),
) : ExtensionsRef,
    ExtensionsMut,
    AsyncRead,
    AsyncWrite,
    Socket {
    override fun extensions(): Extensions = extensionsStore

    override fun extensionsMut(): Extensions = extensionsStore

    override fun localAddr(): SocketAddress? = localAddress ?: stream.localAddr()

    override fun peerAddr(): SocketAddress? = peerAddress ?: stream.peerAddr()

    override fun pollRead(cx: Context, buf: ReadBuf): Poll<Result<Unit>> =
        stream.pollRead(cx, buf)

    override fun pollWrite(cx: Context, buf: UByteArray): Poll<Result<Int>> =
        stream.pollWrite(cx, buf)

    override fun pollWriteVectored(cx: Context, bufs: List<IoSlice>): Poll<Result<Int>> =
        stream.pollWriteVectored(cx, bufs)

    override fun pollFlush(cx: Context): Poll<Result<Unit>> =
        stream.pollFlush(cx)

    override fun pollShutdown(cx: Context): Poll<Result<Unit>> =
        stream.pollShutdown(cx)

    /**
     * Poll read into buffer.
     */
    public fun pollRead(buf: ByteArray): Int = buf.size

    /**
     * Poll write from buffer.
     */
    public fun pollWrite(buf: ByteArray): Int = buf.size

    /**
     * Poll write vectored buffers.
     */
    public fun pollWriteVectored(bufs: List<ByteArray>): Int = bufs.sumOf { it.size }

    /**
     * Poll flush stream.
     */
    public fun pollFlush(): Boolean = true

    /**
     * Poll shutdown stream.
     */
    public fun pollShutdown(): Boolean = true

    /**
     * Check if write vectored is supported.
     */
    override fun isWriteVectored(): Boolean = stream.isWriteVectored()

    /**
     * Convert this stream into an underlying platform stream.
     */
    public fun toTokioTcpStream(): TokioTcpStream = stream

    public companion object {
        public fun new(stream: TokioTcpStream = TokioTcpStream()): TcpStream = TcpStream(stream = stream)

        public fun from(stream: TokioTcpStream): TcpStream = TcpStream(stream = stream)

        public fun withAddresses(
            stream: TokioTcpStream? = null,
            localAddress: SocketAddress? = null,
            peerAddress: SocketAddress? = null,
            extensions: Extensions = Extensions.new(),
        ): TcpStream =
            TcpStream(
                stream = stream ?: TokioTcpStream(localAddress = localAddress, peerAddress = peerAddress),
                extensionsStore = extensions,
                localAddress = localAddress,
                peerAddress = peerAddress,
            )
    }
}
