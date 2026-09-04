package com.douglassantana.sdui_components.di

import com.douglassantana.sdui_components.screen.SduiScreenRenderer
import com.douglassantana.sdui_core.di.sduiCoreModule
import com.douglassantana.sdui_core.factory.ComponentFactory
import com.douglassantana.sdui_runtime.compose.ComponentRenderer
import com.douglassantana.sdui_runtime.di.sduiRuntimeModule
import com.douglassantana.sdui_runtime.renderer.RendererRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.dsl.koinApplication

/**
 * PT: Sobe um grafo Koin isolado (sem tocar no global) com os módulos reais do projeto,
 *     para garantir que a resolução funciona de verdade em runtime — em particular, que
 *     [SduiScreenRenderer] resolve seu [RendererRegistry] lazy sem estourar dependência
 *     circular na hora de montar o próprio RendererRegistry via getAll().
 *
 * EN: Boots an isolated Koin graph (not touching the global one) with the project's real
 *     modules, to make sure resolution actually works at runtime — in particular, that
 *     [SduiScreenRenderer] resolves its lazy [RendererRegistry] without blowing up with a
 *     circular dependency while RendererRegistry itself is being built via getAll().
 */
class SduiComponentsModuleTest {

    @Test
    fun `graph resolves RendererRegistry with all renderers, including SduiScreenRenderer`() {
        val koin = koinApplication {
            modules(sduiCoreModule, sduiRuntimeModule, sduiComponentsModule)
        }.koin

        val renderers = koin.getAll<ComponentRenderer<*>>()
        val factories = koin.getAll<ComponentFactory<*>>()

        assertTrue(renderers.any { it is SduiScreenRenderer })
        assertEquals(setOf("text", "header", "body", "bottom", "screen"), factories.map { it.type() }.toSet())

        // Resolving RendererRegistry itself triggers getAll<ComponentRenderer<*>>(), which
        // constructs SduiScreenRenderer — if its Lazy<RendererRegistry> weren't lazy, this
        // would throw a circular dependency error right here.
        val registry = koin.get<RendererRegistry>()
        assertTrue(registry is RendererRegistry)
    }
}
