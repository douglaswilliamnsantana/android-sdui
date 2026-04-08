package com.douglassantana.sdui_components.extensions

import androidx.compose.ui.text.font.FontStyle
import org.junit.Assert.assertEquals
import org.junit.Test

class FontStyleExtensionsTest {

    @Test
    fun `italic maps to FontStyle Italic`() {
        assertEquals(FontStyle.Italic, "italic".toFontStyle())
    }

    @Test
    fun `normal maps to FontStyle Normal`() {
        assertEquals(FontStyle.Normal, "normal".toFontStyle())
    }

    @Test
    fun `unknown token falls back to FontStyle Normal`() {
        assertEquals(FontStyle.Normal, "oblique".toFontStyle())
    }

    @Test
    fun `empty string falls back to FontStyle Normal`() {
        assertEquals(FontStyle.Normal, "".toFontStyle())
    }
}
