package com.douglassantana.shared

import com.douglassantana.sdui_core.Node
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NodeReaderTest {

    private fun nodeWithProps(vararg pairs: Pair<String, JsonPrimitive>) =
        Node(type = "test", props = JsonObject(pairs.toMap()))

    @Test
    fun `type returns node type`() {
        val reader = NodeReader(Node("text"))
        assertEquals("text", reader.type)
    }

    @Test
    fun `children wraps each child in NodeReader`() {
        val parent = Node("column", children = listOf(Node("text"), Node("button")))
        val reader = NodeReader(parent)
        assertEquals(2, reader.children.size)
        assertEquals("text", reader.children[0].type)
        assertEquals("button", reader.children[1].type)
    }

    @Test
    fun `stringProp returns string value`() {
        val reader = NodeReader(nodeWithProps("label" to JsonPrimitive("Hello")))
        assertEquals("Hello", reader.stringProp("label"))
    }

    @Test
    fun `stringProp returns null for missing key`() {
        val reader = NodeReader(Node("test"))
        assertNull(reader.stringProp("missing"))
    }

    @Test
    fun `stringProp returns null for numeric value`() {
        val reader = NodeReader(nodeWithProps("size" to JsonPrimitive(22)))
        assertNull(reader.stringProp("size"))
    }

    @Test
    fun `doubleProp returns double value`() {
        val reader = NodeReader(nodeWithProps("fontSize" to JsonPrimitive(22.5)))
        assertEquals(22.5, reader.doubleProp("fontSize"))
    }

    @Test
    fun `doubleProp returns null for missing key`() {
        val reader = NodeReader(Node("test"))
        assertNull(reader.doubleProp("missing"))
    }

    @Test
    fun `doubleProp returns null for non-numeric string`() {
        val reader = NodeReader(nodeWithProps("size" to JsonPrimitive("large")))
        assertNull(reader.doubleProp("size"))
    }

    @Test
    fun `boolProp returns true`() {
        val reader = NodeReader(nodeWithProps("visible" to JsonPrimitive(true)))
        assertEquals(true, reader.boolProp("visible"))
    }

    @Test
    fun `boolProp returns false`() {
        val reader = NodeReader(nodeWithProps("disabled" to JsonPrimitive(false)))
        assertEquals(false, reader.boolProp("disabled"))
    }

    @Test
    fun `boolProp returns null for missing key`() {
        val reader = NodeReader(Node("test"))
        assertNull(reader.boolProp("missing"))
    }

    @Test
    fun `objectProp returns sub-reader for nested object`() {
        val style = JsonObject(mapOf("color" to JsonPrimitive("#FFF")))
        val node = Node(type = "text", props = JsonObject(mapOf("style" to style)))
        val reader = NodeReader(node)
        val styleReader = reader.objectProp("style")
        assertEquals("#FFF", styleReader?.stringProp("color"))
    }

    @Test
    fun `objectProp returns null for missing key`() {
        val reader = NodeReader(Node("test"))
        assertNull(reader.objectProp("style"))
    }

    @Test
    fun `objectProp returns null for non-object value`() {
        val reader = NodeReader(nodeWithProps("style" to JsonPrimitive("flat")))
        assertNull(reader.objectProp("style"))
    }
}
