package com.douglassantana.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NodeDtoTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `default values are empty`() {
        val dto = NodeDto()
        assertEquals("", dto.type)
        assertTrue(dto.props.isEmpty())
        assertTrue(dto.components.isEmpty())
    }

    @Test
    fun `deserializes type correctly`() {
        val dto = json.decodeFromString<NodeDto>("""{"type":"text"}""")
        assertEquals("text", dto.type)
    }

    @Test
    fun `deserializes props correctly`() {
        val dto = json.decodeFromString<NodeDto>(
            """{"type":"text","props":{"text":"Hello"}}"""
        )
        assertEquals(JsonPrimitive("Hello"), dto.props["text"])
    }

    @Test
    fun `deserializes nested components`() {
        val dto = json.decodeFromString<NodeDto>(
            """{"type":"column","components":[{"type":"text"},{"type":"button"}]}"""
        )
        assertEquals(2, dto.components.size)
        assertEquals("text",   dto.components[0].type)
        assertEquals("button", dto.components[1].type)
    }

    @Test
    fun `deserializes deeply nested components`() {
        val dto = json.decodeFromString<NodeDto>(
            """{"type":"column","components":[{"type":"row","components":[{"type":"text"}]}]}"""
        )
        val row = dto.components.single()
        assertEquals("row",  row.type)
        assertEquals("text", row.components.single().type)
    }

    @Test
    fun `ignores unknown fields`() {
        val dto = json.decodeFromString<NodeDto>(
            """{"type":"text","unknown":"field","props":{}}"""
        )
        assertEquals("text", dto.type)
    }

    @Test
    fun `missing components defaults to empty list`() {
        val dto = json.decodeFromString<NodeDto>("""{"type":"text"}""")
        assertTrue(dto.components.isEmpty())
    }

    @Test
    fun `missing props defaults to empty map`() {
        val dto = json.decodeFromString<NodeDto>("""{"type":"text"}""")
        assertTrue(dto.props.isEmpty())
    }
}
