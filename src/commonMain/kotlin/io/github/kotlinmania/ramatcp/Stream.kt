// port-lint: source stream.rs
package io.github.kotlinmania.ramatcp

import kotlin.reflect.KClass

/**
 * A type map of protocol extensions.
 */
public class Extensions {
    private val entries: MutableList<Entry> = mutableListOf()

    private class Entry(
        val typeId: KClass<*>,
        val value: Any,
    )

    /** Extend this store with the entries from other. */
    public fun extend(other: Extensions): Extensions {
        entries.addAll(other.entries)
        return this
    }

    /** Make a copy of this store. */
    public fun copy(): Extensions {
        val out = Extensions()
        out.entries.addAll(entries)
        return out
    }

    /** Insert a value into this store under its runtime class. */
    public inline fun <reified T : Any> insert(value: T): Extensions {
        insertErased(T::class, value)
        return this
    }

    @PublishedApi
    internal fun insertErased(typeId: KClass<*>, value: Any) {
        entries.add(Entry(typeId, value))
    }

    /** Returns true if this store contains any value of type T. */
    public inline fun <reified T : Any> contains(): Boolean = containsErased(T::class)

    @PublishedApi
    internal fun containsErased(typeId: KClass<*>): Boolean {
        for (i in entries.indices.reversed()) {
            if (entries[i].typeId == typeId) return true
        }
        return false
    }

    /** Returns the most recently inserted value of type T, or null if none. */
    @Suppress("UNCHECKED_CAST")
    public inline fun <reified T : Any> get(): T? = getErased(T::class) as? T

    @PublishedApi
    internal fun getErased(typeId: KClass<*>): Any? {
        for (i in entries.indices.reversed()) {
            val entry = entries[i]
            if (entry.typeId == typeId) return entry.value
        }
        return null
    }

    /** Removes all values of type T from this store. */
    public inline fun <reified T : Any> remove(): Boolean = removeErased(T::class)

    @PublishedApi
    internal fun removeErased(typeId: KClass<*>): Boolean {
        var removed = false
        val iterator = entries.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().typeId == typeId) {
                iterator.remove()
                removed = true
            }
        }
        return removed
    }

    /** Clear all entries from the store. */
    public fun clear() {
        entries.clear()
    }

    /** Returns the number of entries stored. */
    public val size: Int get() = entries.size

    /** Returns true if the store contains no entries. */
    public fun isEmpty(): Boolean = entries.isEmpty()
}

/**
 * Trait implemented by types that provide read access to Extensions.
 */
public interface ExtensionsRef {
    public val extensions: Extensions
}

/**
 * Trait implemented by types that provide mutable access to Extensions.
 */
public interface ExtensionsMut : ExtensionsRef {
    public fun extensionsMut(): Extensions
}

/**
 * An IP address with an associated port.
 */
public data class SocketAddress(
    val ip: String,
    val port: UShort,
) : Comparable<SocketAddress> {
    public fun isIpv6(): Boolean = ip.contains(':')

    override fun toString(): String =
        if (isIpv6()) {
            "[$ip]:$port"
        } else {
            "$ip:$port"
        }

    override fun compareTo(other: SocketAddress): Int {
        val ipCmp = ip.compareTo(other.ip, ignoreCase = true)
        if (ipCmp != 0) return ipCmp
        return port.compareTo(other.port)
    }

    public companion object {
        public fun localIpv4(port: UShort = 8080u): SocketAddress = SocketAddress("127.0.0.1", port)

        public fun localIpv6(port: UShort = 8080u): SocketAddress = SocketAddress("::1", port)

        public fun defaultIpv4(port: UShort = 8080u): SocketAddress = SocketAddress("0.0.0.0", port)

        public fun defaultIpv6(port: UShort = 8080u): SocketAddress = SocketAddress("::", port)

        public fun parse(s: String): SocketAddress {
            val lastColon = s.lastIndexOf(':')
            require(lastColon != -1) { "missing port in socket address: $s" }
            val portStr = s.substring(lastColon + 1)
            val port = portStr.toUShortOrNull() ?: error("invalid port: $portStr")
            val rawHost = s.substring(0, lastColon)
            val ip =
                if (rawHost.startsWith('[') && rawHost.endsWith(']')) {
                    rawHost.substring(1, rawHost.length - 1)
                } else {
                    rawHost
                }
            return SocketAddress(ip, port)
        }
    }
}

/**
 * Trait to read and write bytes asynchronously.
 */
public interface AsyncStream

/**
 * Trait implemented by connection listeners.
 */
public interface Accept {
    public suspend fun accept(): Result<TcpStream>
}

/**
 * Host name or IP address paired with a port number.
 */
public data class HostWithPort(
    val host: String,
    val port: UShort,
) {
    override fun toString(): String =
        if (host.contains(':') && !host.startsWith('[')) {
            "[$host]:$port"
        } else {
            "$host:$port"
        }

    public companion object {
        public fun parse(s: String): HostWithPort {
            val lastColon = s.lastIndexOf(':')
            require(lastColon != -1) { "missing port: $s" }
            val host = s.substring(0, lastColon).trim('[', ']')
            val port = s.substring(lastColon + 1).toUShort()
            return HostWithPort(host, port)
        }
    }
}

/**
 * Socket trait providing local and peer address access.
 */
public interface Socket {
    public fun localAddr(): SocketAddress?

    public fun peerAddr(): SocketAddress?
}

/**
 * TCP stream wrapper around an underlying transport connection.
 */
public class TcpStream(
    public val stream: Any? = null,
    override val extensions: Extensions = Extensions(),
    private val localAddress: SocketAddress? = null,
    private val peerAddress: SocketAddress? = null,
) : ExtensionsRef,
    ExtensionsMut,
    Socket {
    override fun extensionsMut(): Extensions = extensions

    override fun localAddr(): SocketAddress? = localAddress

    override fun peerAddr(): SocketAddress? = peerAddress

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
    public fun isWriteVectored(): Boolean = false

    /**
     * Convert this stream into an underlying platform stream.
     */
    public fun toTokioTcpStream(): Any? = stream

    public companion object {
        public fun new(stream: Any? = null): TcpStream = TcpStream(stream = stream)

        public fun from(stream: Any?): TcpStream = TcpStream(stream = stream)

        public fun withAddresses(
            stream: Any? = null,
            localAddress: SocketAddress? = null,
            peerAddress: SocketAddress? = null,
            extensions: Extensions = Extensions(),
        ): TcpStream =
            TcpStream(
                stream = stream,
                extensions = extensions,
                localAddress = localAddress,
                peerAddress = peerAddress,
            )
    }
}

public typealias TokioTcpStream = TcpStream
