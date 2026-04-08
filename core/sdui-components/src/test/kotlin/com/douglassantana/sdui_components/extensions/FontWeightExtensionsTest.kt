package com.douglassantana.sdui_components.extensions

import androidx.compose.ui.text.font.FontWeight
import org.junit.Assert.assertEquals
import org.junit.Test

class FontWeightExtensionsTest {

    @Test
    fun `thin maps to FontWeight Thin`() {
        assertEquals(FontWeight.Thin, "thin".toFontWeight())
    }

    @Test
    fun `light maps to FontWeight Light`() {
        assertEquals(FontWeight.Light, "light".toFontWeight())
    }

    @Test
    fun `normal maps to FontWeight Normal`() {
        assertEquals(FontWeight.Normal, "normal".toFontWeight())
    }

    @Test
    fun `medium maps to FontWeight Medium`() {
        assertEquals(FontWeight.Medium, "medium".toFontWeight())
    }

    @Test
    fun `semi-bold maps to FontWeight SemiBold`() {
        assertEquals(FontWeight.SemiBold, "semi-bold".toFontWeight())
    }

    @Test
    fun `bold maps to FontWeight Bold`() {
        assertEquals(FontWeight.Bold, "bold".toFontWeight())
    }

    @Test
    fun `unknown token falls back to FontWeight Normal`() {
        assertEquals(FontWeight.Normal, "extra-heavy".toFontWeight())
    }

    @Test
    fun `empty string falls back to FontWeight Normal`() {
        assertEquals(FontWeight.Normal, "".toFontWeight())
    }
}
