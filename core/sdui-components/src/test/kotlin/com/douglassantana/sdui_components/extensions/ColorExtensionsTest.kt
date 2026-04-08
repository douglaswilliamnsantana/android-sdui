package com.douglassantana.sdui_components.extensions

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorExtensionsTest {

    // region toColor

    @Test
    fun `toColor returns Unspecified for null`() {
        val color: String? = null
        assertEquals(Color.Unspecified, color.toColor())
    }

    // endregion

    // region toColorOrDefault

    @Test
    fun `toColorOrDefault returns default for null`() {
        val color: String? = null
        assertEquals(Color.Red, color.toColorOrDefault(Color.Red))
    }

    // endregion

    // region toColorOrTransparent

    @Test
    fun `toColorOrTransparent returns Transparent for null`() {
        val color: String? = null
        assertEquals(Color.Transparent, color.toColorOrTransparent())
    }

    @Test
    fun `toColorOrTransparent returns Transparent for invalid hex`() {
        assertEquals(Color.Transparent, "invalid".toColorOrTransparent())
    }

    // endregion
}
