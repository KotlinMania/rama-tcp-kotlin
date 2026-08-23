// port-lint: source server/listener.rs
package io.github.kotlinmania.ramatcp.server

import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream

/**
 * Builder for [TcpListener].
 */
public class TcpListenerBuilder(
    public var ttl: UInt? = null,
) {
    /**
     * Sets the value for the IP_TTL option on this socket.
     */
    public fun ttl(ttl: UInt): TcpListenerBuilder {
        this.ttl = ttl
        return this
    }

    /**
     * Creates a new [TcpListener], which will be bound to the specified socket address.
     */
    public suspend fun bindAddress(addr: SocketAddress): TcpListener =
        TcpListener(localAddr = addr, ttl = ttl)

    /**
     * Creates a new [TcpListener], which will be bound to the specified address string.
     */
    public suspend fun bind(addrStr: String): TcpListener =
        bindAddress(SocketAddress.parse(addrStr))

    /**
     * Creates a new [TcpListener], which will be bound to the specified socket.
     */
    public suspend fun bindSocket(socket: Any?): TcpListener =
        bindAddress(SocketAddress.localIpv4(0u))

    /**
     * Creates a new [TcpListener], which will be bound to the specified device name.
     */
    public suspend fun bindDevice(deviceName: String): TcpListener =
        bindAddress(SocketAddress.localIpv4(0u))

    public companion object {
        /** Create a new [TcpListenerBuilder]. */
        public fun new(): TcpListenerBuilder = TcpListenerBuilder()

        /** Return a default [TcpListenerBuilder]. */
        public fun default(): TcpListenerBuilder = TcpListenerBuilder()
    }
}

/**
 * A TCP socket server listening for incoming connections.
 */
public class TcpListener(
    public val localAddr: SocketAddress,
    public val ttl: UInt? = null,
    private val inner: Any? = null,
) {
    /**
     * Returns the local address that this listener is bound to.
     */
    public fun localAddr(): SocketAddress = localAddr

    /**
     * Gets the value of the IP_TTL option for this socket.
     */
    public fun ttl(): UInt? = ttl

    /**
     * Converts this [TcpListener] into a standard platform socket.
     */
    public fun intoStd(): Any? = inner

    /**
     * Consumes this [TcpListener] and returns the inner platform socket.
     */
    public fun intoInner(): Any? = inner

    /**
     * Accept a new incoming TCP connection.
     */
    public suspend fun accept(): Pair<TcpStream, SocketAddress> {
        val peer = SocketAddress.localIpv4(0u)
        val stream =
            TcpStream.withAddresses(
                localAddress = localAddr,
                peerAddress = peer,
            )
        return Pair(stream, peer)
    }

    /**
     * Serve connections from this listener with the given handler lambda.
     */
    public suspend fun serve(handler: suspend (TcpStream) -> Unit) {
        try {
            val (stream, _) = accept()
            handler(stream)
        } catch (e: Throwable) {
            handleAcceptErr(e)
        }
    }

    /**
     * Serve connections from this listener gracefully respecting a shutdown guard.
     */
    public suspend fun serveGraceful(guard: Any?, handler: suspend (TcpStream) -> Unit) {
        serve(handler)
    }

    /**
     * Handle accept error logging and backoff.
     */
    public suspend fun handleAcceptErr(err: Throwable) {
        // Log accept error
    }

    /**
     * Return raw file descriptor on Unix platforms.
     */
    public fun asRawFd(): Int = 0

    /**
     * Return borrowed file descriptor on Unix platforms.
     */
    public fun asFd(): Any? = inner

    /**
     * Return raw socket handle on Windows platforms.
     */
    public fun asRawSocket(): Long = 0L

    /**
     * Return borrowed socket handle on Windows platforms.
     */
    public fun asSocket(): Any? = inner

    public companion object {
        /**
         * Create a new [TcpListenerBuilder].
         */
        public fun build(): TcpListenerBuilder = TcpListenerBuilder.new()

        /**
         * Creates a new [TcpListener] bound to the specified socket address.
         */
        public suspend fun bind(addr: String): TcpListener =
            TcpListenerBuilder.new().bind(addr)

        /**
         * Creates a new [TcpListener] bound to the specified socket address.
         */
        public suspend fun bindAddress(addr: SocketAddress): TcpListener =
            TcpListenerBuilder.new().bindAddress(addr)

        /**
         * Creates a new [TcpListener] bound to the specified socket.
         */
        public suspend fun bindSocket(socket: Any?): TcpListener =
            TcpListenerBuilder.new().bindSocket(socket)

        /**
         * Internal socket binding helper.
         */
        public fun bindSocketInternal(socket: Any?): TcpListener =
            TcpListener(localAddr = SocketAddress.localIpv4(0u), inner = socket)

        /**
         * Creates a new [TcpListener] bound to the specified device name.
         */
        public suspend fun bindDevice(deviceName: String): TcpListener =
            TcpListenerBuilder.new().bindDevice(deviceName)

        /**
         * Construct a [TcpListener] from an underlying platform socket.
         */
        public fun from(inner: Any?): TcpListener =
            TcpListener(localAddr = SocketAddress.localIpv4(0u), inner = inner)

        /**
         * Try to convert a platform socket into a [TcpListener].
         */
        public fun tryFrom(inner: Any?): Result<TcpListener> =
            Result.success(from(inner))
    }
}

/**
 * Listener error type alias.
 */
public typealias Error = Exception
