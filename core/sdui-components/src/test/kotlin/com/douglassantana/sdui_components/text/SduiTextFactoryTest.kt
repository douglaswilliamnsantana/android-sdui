package com.douglassantana.sdui_components.text

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.context.SDUIContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Test

class SduiTextFactoryTest {

    private val factory = SduiTextFactory()

    @Test
    fun `type returns text`() {
        assertEquals("text", factory.type())
    }

    @Test
    fun `parseProps extracts text field`() {
        val node = Node(
            type = "text",
            props = JsonObject(mapOf("text" to JsonPrimitive("Hello SDUI")))
        )
        val props = factory.parseProps(node)
        assertEquals("Hello SDUI", props.text)
    }

    @Test
    fun `parseProps uses empty string as default text`() {
        val node = Node(type = "text", props = JsonObject(emptyMap()))
        val props = factory.parseProps(node)
        assertEquals("", props.text)
    }

    @Test
    fun `parseProps style is null when absent`() {
        val node = Node(type = "text", props = JsonObject(emptyMap()))
        val props = factory.parseProps(node)
        assertEquals(null, props.style)
    }

    @Test
    fun `create returns SduiText with correct value`() {
        val node = Node(
            type = "text",
            props = JsonObject(mapOf("text" to JsonPrimitive("World")))
        )
        val component = factory.build(node, SDUIContext()) as SduiText
        assertEquals("World", component.value)
    }

    @Test
    fun `create passes children to SduiText`() {
        val child = SduiText("child")
        val node = Node(type = "text", props = JsonObject(mapOf("text" to JsonPrimitive("parent"))))
        val component = factory.create(factory.parseProps(node), SDUIContext(), listOf(child)) as SduiText
        assertEquals(1, component.children.size)
        assertEquals(child, component.children.first())
    }

    @Test
    fun `create uses default SduiTextStyle when style prop is absent`() {
        val node = Node(type = "text", props = JsonObject(mapOf("text" to JsonPrimitive("x"))))
        val component = factory.build(node, SDUIContext()) as SduiText
        assertEquals(SduiTextStyle(), component.style)
    }
}
