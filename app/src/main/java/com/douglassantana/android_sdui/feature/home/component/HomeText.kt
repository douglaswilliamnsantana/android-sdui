package com.douglassantana.android_sdui.feature.home.component

import com.douglassantana.sdui_core.UIComponent

/**
 * Componente SDUI que representa um texto simples na tela.
 *
 * Criado por [HomeTextFactory]
 * a partir de um [Node] do tipo `"text"`.
 * Renderizado por [HomeTextRenderer].
 *
 * @property value O conteúdo textual a ser exibido. Lido da prop `"text"` do Node.
 * @property children Filhos aninhados, processados e renderizados recursivamente.
 *
 * ---
 *
 * SDUI component that represents a simple text on screen.
 *
 * Created by [HomeTextFactory]
 * from a [Node] of type `"text"`.
 * Rendered by [HomeTextRenderer].
 *
 * @property value The text content to be displayed. Read from the Node's `"text"` prop.
 * @property children Nested children, processed and rendered recursively.
 */
data class HomeText(
    val value: String,
    override val children: List<UIComponent> = emptyList()
) : UIComponent