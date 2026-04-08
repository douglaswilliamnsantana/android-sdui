package com.douglassantana.model.style

import kotlin.test.Test
import kotlin.test.assertEquals

class IMarginTest {

    @Test
    fun `default values are all zero`() {
        val margin = IMargin()
        assertEquals(0, margin.start)
        assertEquals(0, margin.end)
        assertEquals(0, margin.top)
        assertEquals(0, margin.bottom)
    }

    @Test
    fun `custom values are preserved`() {
        val margin = IMargin(start = 8, end = 16, top = 24, bottom = 32)
        assertEquals(8,  margin.start)
        assertEquals(16, margin.end)
        assertEquals(24, margin.top)
        assertEquals(32, margin.bottom)
    }

    @Test
    fun `equal margins are equal`() {
        assertEquals(IMargin(4, 4, 4, 4), IMargin(4, 4, 4, 4))
    }

    @Test
    fun `different margins are not equal`() {
        val a = IMargin(start = 8)
        val b = IMargin(start = 16)
        assert(a != b)
    }

    @Test
    fun `copy preserves values`() {
        val original = IMargin(start = 8, end = 16, top = 4, bottom = 2)
        val copy = original.copy(top = 99)
        assertEquals(8,  copy.start)
        assertEquals(16, copy.end)
        assertEquals(99, copy.top)
        assertEquals(2,  copy.bottom)
    }
}
