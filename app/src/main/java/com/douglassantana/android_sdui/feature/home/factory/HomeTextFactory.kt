package com.douglassantana.android_sdui.feature.home.factory

import com.douglassantana.android_sdui.feature.home.component.HomeText
import com.douglassantana.android_sdui.sduiCore.Node
import com.douglassantana.android_sdui.sduiCore.UIComponent
import com.douglassantana.android_sdui.sduiCore.context.SDUIContext
import com.douglassantana.android_sdui.sduiCore.factory.ComponentFactory
import javax.inject.Inject

/**
 * Factory responsável por criar [HomeText] a partir de um [Node] do tipo `"text"`.
 *
 * Lê a prop `"text"` do [Node] para preencher o valor textual do componente.
 * Caso a prop esteja ausente ou seja de tipo inesperado, usa string vazia como fallback.
 * Os filhos resolvidos pelo [com.douglassantana.android_sdui.sduiCore.registry.ComponentRegistry]
 * são repassados diretamente ao [HomeText].
 *
 * Registrada no grafo Hilt via [com.douglassantana.android_sdui.feature.home.di.HomeSDUIModule].
 *
 * ---
 *
 * Factory responsible for creating [HomeText] from a [Node] of type `"text"`.
 *
 * Reads the `"text"` prop from the [Node] to fill the component's text value.
 * If the prop is absent or of an unexpected type, an empty string is used as fallback.
 * Children resolved by [com.douglassantana.android_sdui.sduiCore.registry.ComponentRegistry]
 * are passed directly to [HomeText].
 *
 * Registered in the Hilt graph via [com.douglassantana.android_sdui.feature.home.di.HomeSDUIModule].
 */
class HomeTextFactory @Inject constructor() : ComponentFactory {

    override fun type() = "text"

    override fun create(
        node: Node,
        context: SDUIContext,
        children: List<UIComponent>
    ): UIComponent {
        return HomeText(
            value = node.props["text"] as? String ?: "",
            children = children
        )
    }
}