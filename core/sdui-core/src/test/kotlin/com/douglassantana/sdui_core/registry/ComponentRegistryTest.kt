package com.douglassantana.sdui_core.registry

import com.douglassantana.sdui_core.IProps
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.UnknownComponent
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.ComponentFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ComponentRegistryTest {

    // region fakes

    private data object FakeProps : IProps
    private data class FakeComponent(val label: String) : UIComponent

    private fun fakeFactory(typeName: String, label: String) =
        object : ComponentFactory<FakeProps> {
            override fun type() = typeName
            override fun parseProps(node: Node) = FakeProps
            override fun create(
                props: FakeProps,
                context: SDUIContext,
                children: List<UIComponent>,
            ) = FakeComponent(label)
        }

    // endregion

    @Test
    fun `create returns component from matching factory`() {
        val registry = ComponentRegistry(setOf(fakeFactory("text", "hello")))
        val result = registry.create(Node("text"), SDUIContext())
        assertEquals(FakeComponent("hello"), result)
    }

    @Test
    fun `create returns UnknownComponent when no factory matches`() {
        val registry = ComponentRegistry(emptySet())
        val result = registry.create(Node("unknown"), SDUIContext())
        assertTrue(result is UnknownComponent)
    }

    @Test
    fun `UnknownComponent holds the unregistered type`() {
        val registry = ComponentRegistry(emptySet())
        val result = registry.create(Node("mystery"), SDUIContext()) as UnknownComponent
        assertEquals("mystery", result.type)
    }

    @Test
    fun `create resolves children recursively`() {
        val childFactory = object : ComponentFactory<FakeProps> {
            override fun type() = "child"
            override fun parseProps(node: Node) = FakeProps
            override fun create(
                props: FakeProps,
                context: SDUIContext,
                children: List<UIComponent>,
            ) = FakeComponent("child")
        }

        var receivedChildren: List<UIComponent> = emptyList()
        val parentFactory = object : ComponentFactory<FakeProps> {
            override fun type() = "parent"
            override fun parseProps(node: Node) = FakeProps
            override fun create(
                props: FakeProps,
                context: SDUIContext,
                children: List<UIComponent>,
            ): UIComponent {
                receivedChildren = children
                return FakeComponent("parent")
            }
        }

        val registry = ComponentRegistry(setOf(parentFactory, childFactory))
        val parentNode = Node("parent", children = listOf(Node("child")))
        registry.create(parentNode, SDUIContext())

        assertEquals(1, receivedChildren.size)
        assertEquals(FakeComponent("child"), receivedChildren.first())
    }

    @Test
    fun `create passes context to factory`() {
        val context = SDUIContext(languageTag = "pt-BR")
        var capturedContext: SDUIContext? = null

        val factory = object : ComponentFactory<FakeProps> {
            override fun type() = "text"
            override fun parseProps(node: Node) = FakeProps
            override fun create(
                props: FakeProps,
                context: SDUIContext,
                children: List<UIComponent>,
            ): UIComponent {
                capturedContext = context
                return FakeComponent("x")
            }
        }

        val registry = ComponentRegistry(setOf(factory))
        registry.create(Node("text"), context)
        assertEquals("pt-BR", capturedContext?.languageTag)
    }
}
