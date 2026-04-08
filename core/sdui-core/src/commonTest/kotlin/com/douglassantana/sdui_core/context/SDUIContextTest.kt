package com.douglassantana.sdui_core.context

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SDUIContextTest {

    @Test
    fun `default actionHandler is null`() {
        assertNull(SDUIContext().actionHandler)
    }

    @Test
    fun `default languageTag is en`() {
        assertEquals("en", SDUIContext().languageTag)
    }

    @Test
    fun `default extras is empty`() {
        assertTrue(SDUIContext().extras.isEmpty())
    }

    @Test
    fun `custom languageTag is preserved`() {
        assertEquals("pt-BR", SDUIContext(languageTag = "pt-BR").languageTag)
    }

    @Test
    fun `custom extras are preserved`() {
        val ctx = SDUIContext(extras = mapOf("userId" to "42"))
        assertEquals("42", ctx.extras["userId"])
    }

    @Test
    fun `two default contexts are equal`() {
        assertEquals(SDUIContext(), SDUIContext())
    }

    @Test
    fun `contexts with different languageTags are not equal`() {
        assert(SDUIContext(languageTag = "en") != SDUIContext(languageTag = "pt-BR"))
    }
}
