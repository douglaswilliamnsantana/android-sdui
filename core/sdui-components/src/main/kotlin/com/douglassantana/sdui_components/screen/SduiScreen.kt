package com.douglassantana.sdui_components.screen

import com.douglassantana.sdui_core.UIComponent

/**
 * PT: Estrutura de tela com três regiões fixas. [children] fica vazio de propósito —
 *     [SduiScreenRenderer] renderiza o conteúdo de cada slot diretamente, então o
 *     loop automático de filhos do RendererRegistry não teria o que fazer aqui.
 *
 * EN: Screen structure with three fixed regions. [children] is intentionally empty —
 *     [SduiScreenRenderer] renders each slot's content directly, so RendererRegistry's
 *     automatic children loop has nothing left to do here.
 */
data class SduiScreen(
    val header: SduiHeader?,
    val body: SduiBody?,
    val bottom: SduiBottom?,
) : UIComponent
