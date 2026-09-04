package com.douglassantana.sdui_components.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class IconExtensionsTest {

    @Test
    fun `back maps to ArrowBack`() {
        assertEquals(Icons.AutoMirrored.Filled.ArrowBack, "back".toIconVector())
    }

    @Test
    fun `close maps to Close`() {
        assertEquals(Icons.Filled.Close, "close".toIconVector())
    }

    @Test
    fun `menu maps to Menu`() {
        assertEquals(Icons.Filled.Menu, "menu".toIconVector())
    }

    @Test
    fun `search maps to Search`() {
        assertEquals(Icons.Filled.Search, "search".toIconVector())
    }

    @Test
    fun `more maps to MoreVert`() {
        assertEquals(Icons.Filled.MoreVert, "more".toIconVector())
    }

    @Test
    fun `unknown name returns null`() {
        assertNull("rocket".toIconVector())
    }

    @Test
    fun `null returns null`() {
        assertNull((null as String?).toIconVector())
    }
}
