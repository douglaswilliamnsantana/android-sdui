package com.douglassantana.sdui_core

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NodeTest {

    @Test
    fun `default props is empty JsonObject`() {
        val node = Node(type = "text")
        assertTrue(node.props.isEmpty())
    }

    @Test
    fun `default children is empty list`() {
        val node = Node(type = "text")
        assertTrue(node.children.isEmpty())
    }

    @Test
    fun `type is preserved`() {
        assertEquals("column", Node(type = "column").type)
    }

    @Test
    fun `props are preserved`() {
        val props = JsonObject(mapOf("text" to JsonPrimitive("Hello")))
        val node = Node(type = "text", props = props)
        assertEquals(JsonPrimitive("Hello"), node.props["text"])
    }

    @Test
    fun `children are preserved`() {
        val children = listOf(Node("text"), Node("button"))
        val node = Node(type = "column", children = children)
        assertEquals(2, node.children.size)
        assertEquals("text",   node.children[0].type)
        assertEquals("button", node.children[1].type)
    }

    @Test
    fun `nodes with same values are equal`() {
        val a = Node("text")
        val b = Node("text")
        assertEquals(a, b)
    }

    @Test
    fun `copy produces independent node`() {
        val original = Node("text")
        val copy = original.copy(type = "button")
        assertEquals("button", copy.type)
        assertEquals("text", original.type)
    }
}
