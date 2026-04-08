package com.douglassantana.shared.theme

import kotlin.test.Test
import kotlin.test.assertEquals

class SduiColorTest {

    @Test
    fun `fromArgbHex parses opaque white`() {
        val color = SduiColor.fromArgbHex(0xFFFFFFFF)
        assertEquals(1.0, color.alpha, 0.001)
        assertEquals(1.0, color.red, 0.001)
        assertEquals(1.0, color.green, 0.001)
        assertEquals(1.0, color.blue, 0.001)
    }

    @Test
    fun `fromArgbHex parses opaque black`() {
        val color = SduiColor.fromArgbHex(0xFF000000)
        assertEquals(1.0, color.alpha, 0.001)
        assertEquals(0.0, color.red, 0.001)
        assertEquals(0.0, color.green, 0.001)
        assertEquals(0.0, color.blue, 0.001)
    }

    @Test
    fun `fromArgbHex parses fully transparent`() {
        val color = SduiColor.fromArgbHex(0x00000000)
        assertEquals(0.0, color.alpha, 0.001)
    }

    @Test
    fun `fromArgbHex parses red channel`() {
        val color = SduiColor.fromArgbHex(0xFFFF0000)
        assertEquals(1.0, color.red, 0.001)
        assertEquals(0.0, color.green, 0.001)
        assertEquals(0.0, color.blue, 0.001)
    }

    @Test
    fun `fromArgbHex parses green channel`() {
        val color = SduiColor.fromArgbHex(0xFF00FF00)
        assertEquals(0.0, color.red, 0.001)
        assertEquals(1.0, color.green, 0.001)
        assertEquals(0.0, color.blue, 0.001)
    }

    @Test
    fun `fromArgbHex parses blue channel`() {
        val color = SduiColor.fromArgbHex(0xFF0000FF)
        assertEquals(0.0, color.red, 0.001)
        assertEquals(0.0, color.green, 0.001)
        assertEquals(1.0, color.blue, 0.001)
    }

    @Test
    fun `fromArgbHex normalizes channel to 0-1 range`() {
        val color = SduiColor.fromArgbHex(0xFF804020)
        assertEquals(0x80.toDouble() / 255.0, color.red, 0.001)
        assertEquals(0x40.toDouble() / 255.0, color.green, 0.001)
        assertEquals(0x20.toDouble() / 255.0, color.blue, 0.001)
    }

    @Test
    fun `equal colors are equal`() {
        val a = SduiColor.fromArgbHex(0xFF1A202C)
        val b = SduiColor.fromArgbHex(0xFF1A202C)
        assertEquals(a, b)
    }
}
