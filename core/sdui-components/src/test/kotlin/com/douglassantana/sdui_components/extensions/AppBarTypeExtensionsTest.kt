package com.douglassantana.sdui_components.extensions

import com.douglassantana.designsystem.components.appbar.AppBarType
import org.junit.Assert.assertEquals
import org.junit.Test

class AppBarTypeExtensionsTest {

    @Test
    fun `small maps to AppBarType Small`() {
        assertEquals(AppBarType.Small, "small".toAppBarType())
    }

    @Test
    fun `center-aligned maps to AppBarType CenterAligned`() {
        assertEquals(AppBarType.CenterAligned, "center-aligned".toAppBarType())
    }

    @Test
    fun `medium maps to AppBarType Medium`() {
        assertEquals(AppBarType.Medium, "medium".toAppBarType())
    }

    @Test
    fun `large maps to AppBarType Large`() {
        assertEquals(AppBarType.Large, "large".toAppBarType())
    }

    @Test
    fun `unknown token falls back to AppBarType Small`() {
        assertEquals(AppBarType.Small, "huge".toAppBarType())
    }

    @Test
    fun `empty string falls back to AppBarType Small`() {
        assertEquals(AppBarType.Small, "".toAppBarType())
    }
}
