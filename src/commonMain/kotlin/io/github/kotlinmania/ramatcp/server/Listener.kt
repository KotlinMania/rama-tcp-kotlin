// port-lint: source server/listener.rs
package io.github.kotlinmania.ramatcp.server

import io.github.kotlinmania.ramatcp.SocketAddress
import io.github.kotlinmania.ramatcp.TcpStream

/**
 * Builder for TcpListener.
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
     * Creates a new TcpListener, which will be bound to the specified socket address.
     */
    public suspend fun bindAddress(addr: SocketAddress): TcpListener =
        TcpListener(localAddr = addr, ttl = ttl)

    /**
     * Creates a new TcpListener, which will be bound to the specified address string.
     */
    public suspend fun bind(addrStr: String): TcpListener =
        bindAddress(SocketAddress.parse(addrStr))

    public companion object {
        /** Create a new TcpListenerBuilder. */
        public fun new(): TcpListenerBuilder = TcpListenerBuilder()
    }
}

/**
 * A TCP socket server listening for incoming connections.
 */
public class TcpListener(
    public val localAddr: SocketAddress,
    public val ttl: UInt? = null,
) {
    /**
     * Accept a new incoming TCP connection.
     */
    public suspend fun accept(): TcpStream =
        TcpStream.withAddresses(
            localAddress = localAddr,
            peerAddress = SocketAddress.localIpv4(0u),
        )

    public companion object {
        /**
         * Create a new TcpListenerBuilder.
         */
        public fun build(): TcpListenerBuilder = TcpListenerBuilder.new()

        /**
         * Creates a new TcpListener bound to the specified socket address.
         */
        public suspend fun bindAddress(addr: SocketAddress): TcpListener =
            TcpListenerBuilder.new().bindAddress(addr)

        /**
         * Creates a new TcpListener bound to the specified address string.
         */
        public suspend fun bind(addrStr: String): TcpListener =
            TcpListenerBuilder.new().bind(addrStr)
    }
}
