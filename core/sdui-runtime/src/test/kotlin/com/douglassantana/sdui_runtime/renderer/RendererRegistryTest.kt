package com.douglassantana.sdui_runtime.renderer

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_runtime.compose.ComponentRenderer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.reflect.KClass

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RendererRegistryTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // region fakes

    private data class TextComponent(val value: String) : UIComponent

    private class TextRenderer : ComponentRenderer<TextComponent> {
        override val type: KClass<TextComponent> = TextComponent::class

        @androidx.compose.runtime.Composable
        override fun Render(component: TextComponent) {
            Text(text = component.value)
        }
    }

    // endregion

    @Test
    fun `Render displays content from matching renderer`() {
        val registry = RendererRegistry(setOf(TextRenderer()))

        composeTestRule.setContent {
            registry.Render(TextComponent("Hello SDUI"))
        }

        composeTestRule.onNodeWithText("Hello SDUI").assertExists()
    }

    @Test
    fun `Render does not crash when no renderer is registered`() {
        val registry = RendererRegistry(emptySet())

        composeTestRule.setContent {
            registry.Render(TextComponent("ignored"))
        }

        // No exception means the fallback (Log.w + return) worked
    }

    @Test
    fun `Render displays children recursively`() {
        val parent = TextComponent("parent").apply { }
        val child = TextComponent("child")

        // UIComponent with children via a wrapper
        data class ContainerComponent(
            val label: String,
            override val children: List<UIComponent> = emptyList(),
        ) : UIComponent

        class ContainerRenderer : ComponentRenderer<ContainerComponent> {
            override val type: KClass<ContainerComponent> = ContainerComponent::class

            @androidx.compose.runtime.Composable
            override fun Render(component: ContainerComponent) {
                Text(text = component.label)
            }
        }

        val registry = RendererRegistry(setOf(TextRenderer(), ContainerRenderer()))
        val container = ContainerComponent("parent", children = listOf(TextComponent("child")))

        composeTestRule.setContent {
            registry.Render(container)
        }

        composeTestRule.onNodeWithText("parent").assertExists()
        composeTestRule.onNodeWithText("child").assertExists()
    }

    @Test
    fun `Render ignores unregistered child types without crashing`() {
        data class UnknownComponent(val id: String) : UIComponent

        data class ContainerComponent(
            override val children: List<UIComponent> = emptyList(),
        ) : UIComponent

        class ContainerRenderer : ComponentRenderer<ContainerComponent> {
            override val type: KClass<ContainerComponent> = ContainerComponent::class

            @androidx.compose.runtime.Composable
            override fun Render(component: ContainerComponent) {
                Text("container")
            }
        }

        val registry = RendererRegistry(setOf(ContainerRenderer()))
        val container = ContainerComponent(children = listOf(UnknownComponent("x")))

        composeTestRule.setContent {
            registry.Render(container)
        }

        composeTestRule.onNodeWithText("container").assertExists()
    }
}
