// port-lint: source server/listener.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramatcp.server

import io.github.kotlinmania.ramacore.graceful.ShutdownGuard
import io.github.kotlinmania.ramacore.rt.Executor
import io.github.kotlinmania.ramacore.service.Service
import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.conn.isConnectionError
import io.github.kotlinmania.ramanet.socket.DeviceName
import io.github.kotlinmania.ramanet.socket.Interface
import io.github.kotlinmania.ramanet.socket.SocketOptions
import io.github.kotlinmania.ramanet.stream.SocketInfo
import io.github.kotlinmania.ramatcp.TcpStream
import io.github.kotlinmania.socket2.Socket as CoreSocket
import kotlinx.coroutines.delay
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC
import kotlin.time.Duration.Companion.seconds

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
     * Sets the value for the IP_TTL option on this socket.
     */
    public fun setTtl(ttl: UInt): TcpListenerBuilder {
        this.ttl = ttl
        return this
    }

    /**
     * Returns a new builder with the specified IP_TTL option.
     */
    public fun withTtl(ttl: UInt): TcpListenerBuilder =
        TcpListenerBuilder(ttl = ttl)

    /**
     * Creates a new [TcpListener], which will be bound to the specified socket address.
     */
    public suspend fun bindAddress(addr: SocketAddress): TcpListener =
        TcpListener(localAddress = addr, timeToLive = ttl)

    /**
     * Creates a new [TcpListener], which will be bound to the specified address or interface string.
     */
    public suspend fun bind(addrStr: String): TcpListener =
        bind(Interface.parse(addrStr))

    /**
     * Creates a new [TcpListener], which will be bound to the specified [Interface].
     */
    @HiddenFromObjC
    public suspend fun bind(iface: Interface): TcpListener =
        when (iface) {
            is Interface.Address -> bindAddress(iface.socketAddress)
            is Interface.Device -> bindDevice(iface.deviceName)
            is Interface.Socket -> {
                val socket = iface.socketOptions.tryBuildSocket().getOrNull()
                if (socket != null) {
                    socket.listen(4096)
                    bindSocket(socket)
                } else {
                    val addr = iface.socketOptions.address ?: SocketAddress.localIpv4(0u)
                    TcpListener(localAddress = addr, timeToLive = ttl, inner = iface.socketOptions)
                }
            }
        }

    /**
     * Creates a new [TcpListener], which will be bound to the specified [CoreSocket].
     */
    public suspend fun bindSocket(socket: CoreSocket): TcpListener =
        TcpListener.bindSocketInternal(socket).let { listener ->
            TcpListener(
                localAddress = listener.localAddress,
                timeToLive = ttl,
                inner = socket,
            )
        }

    /**
     * Creates a new [TcpListener], which will be bound to the specified socket.
     */
    public suspend fun bindSocket(socket: Any?): TcpListener =
        if (socket is CoreSocket) {
            bindSocket(socket)
        } else {
            TcpListener(localAddress = SocketAddress.localIpv4(0u), timeToLive = ttl, inner = socket)
        }

    /**
     * Creates a new [TcpListener], which will be bound to the specified device name.
     */
    public suspend fun bindDevice(deviceName: String): TcpListener =
        bindDevice(DeviceName(deviceName))

    /**
     * Creates a new [TcpListener], which will be bound to the specified [DeviceName].
     */
    public suspend fun bindDevice(deviceName: DeviceName): TcpListener {
        val opts = SocketOptions.defaultTcp().copy(device = deviceName)
        val built = opts.tryBuildSocket().getOrNull()
        if (built != null) {
            built.listen(4096)
            return bindSocket(built)
        }
        return TcpListener(localAddress = SocketAddress.localIpv4(0u), timeToLive = ttl, inner = deviceName)
    }

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
    public val localAddress: SocketAddress,
    public val timeToLive: UInt? = null,
    private val inner: Any? = null,
) {
    /**
     * Returns the local address that this listener is bound to.
     */
    public fun localAddr(): SocketAddress = localAddress

    /**
     * Gets the value of the IP_TTL option for this socket.
     */
    public fun ttl(): UInt? = timeToLive

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
                localAddress = localAddress,
                peerAddress = peer,
            )
        return Pair(stream, peer)
    }

    /**
     * Serve connections from this listener with the given handler lambda.
     */
    public suspend fun serve(handler: suspend (TcpStream) -> Unit) {
        try {
            val (stream, peer) = accept()
            stream.extensionsMut().insert(SocketInfo.new(localAddr = localAddress, peerAddr = peer))
            stream.extensionsMut().insert(Executor.new())
            handler(stream)
        } catch (e: Throwable) {
            handleAcceptErr(e)
        }
    }

    /**
     * Serve connections from this listener with the given [Service].
     */
    @HiddenFromObjC
    public suspend fun <Output : Any, Error : Any> serve(service: Service<TcpStream, Output, Error>) {
        serve { stream ->
            service.serve(stream)
        }
    }

    /**
     * Serve connections from this listener gracefully respecting a shutdown guard.
     */
    public suspend fun serveGraceful(guard: ShutdownGuard, handler: suspend (TcpStream) -> Unit) {
        if (guard.isCancelled) return
        try {
            val (stream, peer) = accept()
            stream.extensionsMut().insert(SocketInfo.new(localAddr = localAddress, peerAddr = peer))
            stream.extensionsMut().insert(Executor.graceful(guard))
            guard.spawnTask {
                handler(stream)
            }
        } catch (e: Throwable) {
            handleAcceptErr(e)
        }
    }

    /**
     * Serve connections from this listener gracefully with the given [Service].
     */
    @HiddenFromObjC
    public suspend fun <Output : Any, Error : Any> serveGraceful(
        guard: ShutdownGuard,
        service: Service<TcpStream, Output, Error>,
    ) {
        serveGraceful(guard) { stream ->
            service.serve(stream)
        }
    }

    /**
     * Handle accept error logging and backoff.
     */
    public suspend fun handleAcceptErr(err: Throwable) {
        if (!isConnectionError(err)) {
            delay(1.seconds)
        }
    }

    /**
     * Return raw file descriptor on Unix platforms.
     */
    public fun asRawFd(): Int = (inner as? CoreSocket)?.asRaw() ?: 0

    /**
     * Return borrowed file descriptor on Unix platforms.
     */
    public fun asFd(): Any? = inner

    /**
     * Return raw socket handle on Windows platforms.
     */
    public fun asRawSocket(): Long = (inner as? CoreSocket)?.asRaw()?.toLong() ?: 0L

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
         * Creates a new [TcpListener] bound to the specified [Interface].
         */
        @HiddenFromObjC
        public suspend fun bind(iface: Interface): TcpListener =
            TcpListenerBuilder.new().bind(iface)

        /**
         * Creates a new [TcpListener] bound to the specified socket address.
         */
        public suspend fun bindAddress(addr: SocketAddress): TcpListener =
            TcpListenerBuilder.new().bindAddress(addr)

        /**
         * Creates a new [TcpListener] bound to the specified [CoreSocket].
         */
        public suspend fun bindSocket(socket: CoreSocket): TcpListener =
            TcpListenerBuilder.new().bindSocket(socket)

        /**
         * Creates a new [TcpListener] bound to the specified socket.
         */
        public suspend fun bindSocket(socket: Any?): TcpListener =
            TcpListenerBuilder.new().bindSocket(socket)

        /**
         * Internal socket binding helper.
         */
        public fun bindSocketInternal(socket: CoreSocket): TcpListener {
            socket.setNonblocking(true)
            return TcpListener(localAddress = SocketAddress.localIpv4(0u), inner = socket)
        }

        /**
         * Internal socket binding helper.
         */
        public fun bindSocketInternal(socket: Any?): TcpListener =
            if (socket is CoreSocket) {
                bindSocketInternal(socket)
            } else {
                TcpListener(localAddress = SocketAddress.localIpv4(0u), inner = socket)
            }

        /**
         * Creates a new [TcpListener] bound to the specified device name.
         */
        public suspend fun bindDevice(deviceName: String): TcpListener =
            TcpListenerBuilder.new().bindDevice(deviceName)

        /**
         * Creates a new [TcpListener] bound to the specified [DeviceName].
         */
        public suspend fun bindDevice(deviceName: DeviceName): TcpListener =
            TcpListenerBuilder.new().bindDevice(deviceName)

        /**
         * Construct a [TcpListener] from an underlying platform socket.
         */
        public fun from(inner: Any?): TcpListener =
            TcpListener(localAddress = SocketAddress.localIpv4(0u), inner = inner)

        /**
         * Try to convert a platform socket into a [TcpListener].
         */
        @HiddenFromObjC
        public fun tryFrom(inner: Any?): Result<TcpListener> =
            Result.success(from(inner))
    }
}

/**
 * Listener error type alias.
 */
public typealias ListenerError = Exception

/**
 * General error type for listener conversions.
 */
public typealias Error = Exception
