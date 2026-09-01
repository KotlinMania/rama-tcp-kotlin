// port-lint: tests lib.rs
package io.github.kotlinmania.ramatcp

import kotlin.test.Test
import kotlin.test.assertEquals

class LibTest {
    @Test
    fun testVersion() {
        assertEquals("0.2.0", RamaTcp.VERSION)
    }
}
