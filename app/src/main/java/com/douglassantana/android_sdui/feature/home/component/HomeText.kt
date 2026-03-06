package com.douglassantana.android_sdui.feature.home.component

import com.douglassantana.android_sdui.sduiCore.UIComponent

/**
 * Componente SDUI que representa um texto simples na tela.
 *
 * Criado por [com.douglassantana.android_sdui.feature.home.factory.HomeTextFactory]
 * a partir de um [com.douglassantana.android_sdui.sduiCore.Node] do tipo `"text"`.
 * Renderizado por [com.douglassantana.android_sdui.feature.home.renderer.HomeTextRenderer].
 *
 * @property value O conteúdo textual a ser exibido. Lido da prop `"text"` do Node.
 * @property children Filhos aninhados, processados e renderizados recursivamente.
 *
 * ---
 *
 * SDUI component that represents a simple text on screen.
 *
 * Created by [com.douglassantana.android_sdui.feature.home.factory.HomeTextFactory]
 * from a [com.douglassantana.android_sdui.sduiCore.Node] of type `"text"`.
 * Rendered by [com.douglassantana.android_sdui.feature.home.renderer.HomeTextRenderer].
 *
 * @property value The text content to be displayed. Read from the Node's `"text"` prop.
 * @property children Nested children, processed and rendered recursively.
 */
data class HomeText(
    val value: String,
    override val children: List<UIComponent> = emptyList()
) : UIComponent