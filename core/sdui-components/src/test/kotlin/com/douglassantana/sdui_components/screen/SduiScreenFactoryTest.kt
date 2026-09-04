package com.douglassantana.sdui_components.screen

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.context.SDUIContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SduiScreenFactoryTest {

    private val screenFactory = SduiScreenFactory()
    private val headerFactory = SduiHeaderFactory()
    private val bodyFactory = SduiBodyFactory()
    private val bottomFactory = SduiBottomFactory()

    @Test
    fun `type returns screen`() {
        assertEquals("screen", screenFactory.type())
    }

    @Test
    fun `header factory type returns header`() {
        assertEquals("header", headerFactory.type())
    }

    @Test
    fun `body factory type returns body`() {
        assertEquals("body", bodyFactory.type())
    }

    @Test
    fun `bottom factory type returns bottom`() {
        assertEquals("bottom", bottomFactory.type())
    }

    @Test
    fun `create picks each slot by concrete type regardless of order`() {
        val bottom = bottomFactory.build(Node(type = "bottom"), SDUIContext())
        val header = headerFactory.build(Node(type = "header"), SDUIContext())
        val body = bodyFactory.build(Node(type = "body"), SDUIContext())

        val screen = screenFactory.create(
            props = SduiScreenProps,
            context = SDUIContext(),
            children = listOf(bottom, header, body),
        )

        assertEquals(header, screen.header)
        assertEquals(body, screen.body)
        assertEquals(bottom, screen.bottom)
    }

    @Test
    fun `create leaves missing slots null`() {
        val header = headerFactory.build(Node(type = "header"), SDUIContext())

        val screen = screenFactory.create(
            props = SduiScreenProps,
            context = SDUIContext(),
            children = listOf(header),
        )

        assertEquals(header, screen.header)
        assertNull(screen.body)
        assertNull(screen.bottom)
    }

    @Test
    fun `header factory passes children through to SduiHeader`() {
        val grandchild: UIComponent = SduiBody(children = emptyList())
        val header = headerFactory.create(SduiScreenSlotProps, SDUIContext(), listOf(grandchild))
        assertEquals(listOf(grandchild), header.children)
    }
}
