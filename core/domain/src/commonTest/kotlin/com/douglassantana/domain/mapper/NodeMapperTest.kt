package com.douglassantana.domain.mapper

import com.douglassantana.model.NodeDto
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NodeMapperTest {

    @Test
    fun `toNode maps type correctly`() {
        val dto = NodeDto(type = "text")

        val node = NodeMapper.toNode(dto)

        assertEquals("text", node.type)
    }

    @Test
    fun `toNode maps props correctly`() {
        val dto = NodeDto(
            type = "text",
            props = mapOf("text" to JsonPrimitive("Hello SDUI")),
        )

        val node = NodeMapper.toNode(dto)

        assertEquals(JsonPrimitive("Hello SDUI"), node.props["text"])
    }

    @Test
    fun `toNode with no props produces empty JsonObject`() {
        val dto = NodeDto(type = "row")

        val node = NodeMapper.toNode(dto)

        assertTrue(node.props.isEmpty())
    }

    @Test
    fun `toNode maps flat children list`() {
        val dto = NodeDto(
            type = "column",
            components = listOf(
                NodeDto(type = "text"),
                NodeDto(type = "button"),
            ),
        )

        val node = NodeMapper.toNode(dto)

        assertEquals(2, node.children.size)
        assertEquals("text", node.children[0].type)
        assertEquals("button", node.children[1].type)
    }

    @Test
    fun `toNode maps nested children recursively`() {
        val dto = NodeDto(
            type = "column",
            components = listOf(
                NodeDto(
                    type = "row",
                    components = listOf(
                        NodeDto(type = "text"),
                    ),
                ),
            ),
        )

        val node = NodeMapper.toNode(dto)

        val row = node.children.single()
        assertEquals("row", row.type)
        assertEquals("text", row.children.single().type)
    }

    @Test
    fun `toNode with no children produces empty list`() {
        val dto = NodeDto(type = "text")

        val node = NodeMapper.toNode(dto)

        assertTrue(node.children.isEmpty())
    }

    @Test
    fun `toNode preserves multiple props`() {
        val dto = NodeDto(
            type = "text",
            props = mapOf(
                "text"     to JsonPrimitive("Hello"),
                "fontSize" to JsonPrimitive(22),
                "bold"     to JsonPrimitive(true),
            ),
        )

        val node = NodeMapper.toNode(dto)

        assertEquals(3, node.props.size)
        assertEquals(JsonPrimitive("Hello"), node.props["text"])
        assertEquals(JsonPrimitive(22), node.props["fontSize"])
        assertEquals(JsonPrimitive(true), node.props["bold"])
    }
}
