package com.douglassantana.sdui_components.appbar

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.action.ActionHandler
import com.douglassantana.sdui_core.action.UIAction
import com.douglassantana.sdui_core.context.SDUIContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SduiAppBarFactoryTest {

    private val factory = SduiAppBarFactory()

    @Test
    fun `type returns app_bar`() {
        assertEquals("app_bar", factory.type())
    }

    @Test
    fun `parseProps extracts all fields`() {
        val node = Node(
            type = "app_bar",
            props = JsonObject(
                mapOf(
                    "type" to JsonPrimitive("medium"),
                    "title" to JsonPrimitive("Home"),
                    "leftIcon" to JsonPrimitive("back"),
                    "leftAction" to JsonPrimitive("/back"),
                    "rightIcon" to JsonPrimitive("search"),
                    "rightAction" to JsonPrimitive("/search"),
                )
            )
        )
        val props = factory.parseProps(node)
        assertEquals("medium", props.type)
        assertEquals("Home", props.title)
        assertEquals("back", props.leftIcon)
        assertEquals("/back", props.leftAction)
        assertEquals("search", props.rightIcon)
        assertEquals("/search", props.rightAction)
    }

    @Test
    fun `parseProps defaults type to small and title to empty`() {
        val props = factory.parseProps(Node(type = "app_bar", props = JsonObject(emptyMap())))
        assertEquals("small", props.type)
        assertEquals("", props.title)
        assertNull(props.leftIcon)
        assertNull(props.rightIcon)
    }

    @Test
    fun `create carries type, title and icon names through unchanged`() {
        val node = Node(
            type = "app_bar",
            props = JsonObject(
                mapOf(
                    "type" to JsonPrimitive("large"),
                    "title" to JsonPrimitive("Details"),
                    "leftIcon" to JsonPrimitive("close"),
                    "rightIcon" to JsonPrimitive("more"),
                )
            )
        )
        val component = factory.build(node, SDUIContext()) as SduiAppBar
        assertEquals("large", component.type)
        assertEquals("Details", component.title)
        assertEquals("close", component.leftIcon)
        assertEquals("more", component.rightIcon)
    }

    @Test
    fun `create leaves icon actions null when no action prop is present`() {
        val node = Node(type = "app_bar", props = JsonObject(mapOf("leftIcon" to JsonPrimitive("back"))))
        val component = factory.build(node, SDUIContext()) as SduiAppBar
        assertNull(component.leftIconAction)
        assertNull(component.rightIconAction)
    }

    @Test
    fun `create dispatches Navigate through the context actionHandler on click`() {
        val handled = mutableListOf<UIAction>()
        val context = SDUIContext(actionHandler = object : ActionHandler {
            override fun handle(action: UIAction) {
                handled.add(action)
            }
        })
        val node = Node(
            type = "app_bar",
            props = JsonObject(
                mapOf(
                    "leftIcon" to JsonPrimitive("back"),
                    "leftAction" to JsonPrimitive("/back"),
                )
            )
        )
        val component = factory.build(node, context) as SduiAppBar

        component.leftIconAction?.invoke()

        assertEquals(listOf(UIAction.Navigate("/back")), handled)
    }
}
