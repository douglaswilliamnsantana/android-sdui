package com.douglassantana.sdui_core.action

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UIActionTest {

    @Test
    fun `Navigate holds route`() {
        val action = UIAction.Navigate(route = "/home")
        assertEquals("/home", action.route)
    }

    @Test
    fun `Log holds message`() {
        val action = UIAction.Log(message = "clicked")
        assertEquals("clicked", action.message)
    }

    @Test
    fun `Navigate is UIAction`() {
        assertIs<UIAction>(UIAction.Navigate("/home"))
    }

    @Test
    fun `Log is UIAction`() {
        assertIs<UIAction>(UIAction.Log("msg"))
    }

    @Test
    fun `when exhausts all subtypes`() {
        val actions: List<UIAction> = listOf(
            UIAction.Navigate("/home"),
            UIAction.Log("event"),
        )
        val results = actions.map { action ->
            when (action) {
                is UIAction.Navigate -> "navigate:${action.route}"
                is UIAction.Log      -> "log:${action.message}"
            }
        }
        assertEquals(listOf("navigate:/home", "log:event"), results)
    }

    @Test
    fun `Navigate equal when same route`() {
        assertEquals(UIAction.Navigate("/a"), UIAction.Navigate("/a"))
    }

    @Test
    fun `Log equal when same message`() {
        assertEquals(UIAction.Log("x"), UIAction.Log("x"))
    }
}
