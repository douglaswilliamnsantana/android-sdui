package com.douglassantana.android_sdui.feature.home.renderer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.douglassantana.android_sdui.feature.home.component.HomeText
import com.douglassantana.sdui_runtime.compose.ComponentRenderer
import javax.inject.Inject

/**
 * Renderer responsável por desenhar um [HomeText] em Jetpack Compose.
 *
 * Implementa [ComponentRenderer] para o tipo [HomeText], declarando [type] como
 * `HomeText::class` — a chave usada pelo
 * [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry] para
 * rotear o componente a este renderer.
 *
 * A renderização dos filhos é feita automaticamente pelo [RendererRegistry] após
 * a chamada a [Render], não sendo necessário tratá-los aqui.
 *
 * Registrada no grafo Hilt via [com.douglassantana.android_sdui.feature.home.di.HomeSDUIModule].
 *
 * ---
 *
 * Renderer responsible for drawing a [HomeText] in Jetpack Compose.
 *
 * Implements [ComponentRenderer] for the [HomeText] type, declaring [type] as
 * `HomeText::class` — the key used by
 * [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry] to
 * route the component to this renderer.
 *
 * Children rendering is handled automatically by [RendererRegistry] after
 * [Render] is called, so there is no need to handle them here.
 *
 * Registered in the Hilt graph via [cHomeSDUIModule].
 */
class HomeTextRenderer @Inject constructor() :
    ComponentRenderer<HomeText> {

    override val type = HomeText::class

    @Composable
    override fun Render(component: HomeText) {
        Text(text = component.value)
    }
}