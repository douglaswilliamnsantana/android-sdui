package com.douglassantana.sdui_core.factory

import com.douglassantana.sdui_core.IProps
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.context.SDUIContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ComponentFactoryTest {

    // region fakes

    private data object FakeProps : IProps
    private data class FakeComponent(val label: String) : UIComponent

    private val factory = object : ComponentFactory<FakeProps> {
        override fun type() = "fake"
        override fun parseProps(node: Node) = FakeProps
        override fun create(
            props: FakeProps,
            context: SDUIContext,
            children: List<UIComponent>,
        ) = FakeComponent("created")
    }

    // endregion

    @Test
    fun `type returns correct identifier`() {
        assertEquals("fake", factory.type())
    }

    @Test
    fun `build delegates to parseProps and create`() {
        val component = factory.build(Node("fake"), SDUIContext())
        assertEquals(FakeComponent("created"), component)
    }

    @Test
    fun `build passes children to create`() {
        val child = FakeComponent("child")
        var capturedChildren: List<UIComponent> = emptyList()

        val capturingFactory = object : ComponentFactory<FakeProps> {
            override fun type() = "fake"
            override fun parseProps(node: Node) = FakeProps
            override fun create(
                props: FakeProps,
                context: SDUIContext,
                children: List<UIComponent>,
            ): UIComponent {
                capturedChildren = children
                return FakeComponent("root")
            }
        }

        capturingFactory.build(Node("fake"), SDUIContext(), listOf(child))
        assertEquals(1, capturedChildren.size)
        assertSame(child, capturedChildren.first())
    }

    @Test
    fun `build passes context to create`() {
        val context = SDUIContext(languageTag = "pt-BR")
        var capturedContext: SDUIContext? = null

        val capturingFactory = object : ComponentFactory<FakeProps> {
            override fun type() = "fake"
            override fun parseProps(node: Node) = FakeProps
            override fun create(
                props: FakeProps,
                context: SDUIContext,
                children: List<UIComponent>,
            ): UIComponent {
                capturedContext = context
                return FakeComponent("root")
            }
        }

        capturingFactory.build(Node("fake"), context)
        assertEquals("pt-BR", capturedContext?.languageTag)
    }

    @Test
    fun `SduiJson ignores unknown keys`() {
        val result = runCatching {
            SduiJson.decodeFromString<FakeProps>("""{}""")
        }
        // FakeProps doesn't use serialization; what matters is SduiJson config
        assertEquals(true, SduiJson.configuration.ignoreUnknownKeys)
    }

    @Test
    fun `SduiJson coerces input values`() {
        assertEquals(true, SduiJson.configuration.coerceInputValues)
    }
}
