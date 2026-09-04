package com.douglassantana.sdui_components.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.douglassantana.sdui_runtime.compose.ComponentRenderer
import com.douglassantana.sdui_runtime.renderer.RendererRegistry

/**
 * PT: Recebe [RendererRegistry] como [Lazy] em vez de resolvido — o próprio
 *     RendererRegistry é montado a partir de `getAll<ComponentRenderer<*>>()`, o que
 *     inclui este renderer. Resolvê-lo aqui de forma ansiosa criaria uma dependência
 *     circular na construção do grafo Koin; o [Lazy] só é avaliado no primeiro [Render],
 *     quando o RendererRegistry já está pronto.
 *
 * EN: Receives [RendererRegistry] as [Lazy] instead of eagerly resolved — RendererRegistry
 *     itself is built from `getAll<ComponentRenderer<*>>()`, which includes this renderer.
 *     Resolving it eagerly here would create a circular dependency while the Koin graph is
 *     being built; the [Lazy] is only evaluated on the first [Render] call, by which point
 *     RendererRegistry already exists.
 */
class SduiScreenRenderer(
    private val rendererRegistry: Lazy<RendererRegistry>,
) : ComponentRenderer<SduiScreen> {

    override val type = SduiScreen::class

    @Composable
    override fun Render(component: SduiScreen) {
        Scaffold(
            topBar = {
                component.header?.children?.forEach { rendererRegistry.value.Render(it) }
            },
            bottomBar = {
                component.bottom?.children?.forEach { rendererRegistry.value.Render(it) }
            },
        ) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                component.body?.children?.forEach { rendererRegistry.value.Render(it) }
            }
        }
    }
}
