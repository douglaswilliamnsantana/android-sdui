package com.douglassantana.sdui_core.registry

import android.util.Log
import com.douglassantana.sdui_core.IProps
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.UnknownComponent
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.ComponentFactory
import javax.inject.Inject

/**
 * ComponentRegistry — Registro central de factories de componentes SDUI.
 *
 * PT: Recebe via injeção (Hilt multibindings) o conjunto de todas as [ComponentFactory]
 *     registradas e as organiza em um mapa indexado por [ComponentFactory.type].
 *     Ao receber um [Node], resolve a factory correspondente e delega a criação do [UIComponent]
 *     via [ComponentFactory.build] — que orquestra parseProps() + create() internamente.
 *     Se nenhuma factory for encontrada, emite um aviso e retorna [UnknownComponent] como fallback.
 *
 * EN: Receives via injection (Hilt multibindings) the full set of registered
 *     [ComponentFactory] instances and organizes them in a map indexed by [ComponentFactory.type].
 *     When given a [Node], it resolves the matching factory and delegates [UIComponent] creation
 *     via [ComponentFactory.build] — which orchestrates parseProps() + create() internally.
 *     If no factory is found, emits a warning and returns [UnknownComponent] as fallback.
 */
class ComponentRegistry @Inject constructor(
    factories: Set<@JvmSuppressWildcards ComponentFactory<out IProps>>
) {

    private val factoryMap: Map<String, ComponentFactory<out IProps>> by lazy {
        factories.associateBy { it.type() }
    }

    fun create(
        node: Node,
        context: SDUIContext
    ): UIComponent {
        val children = node.children.map { create(it, context) }

        return factoryMap[node.type]
            ?.build(node, context, children)
            ?: run {
                Log.w(
                    "ComponentRegistry",
                    "No factory registered for type '${node.type}'. Falling back to UnknownComponent."
                )
                UnknownComponent(node.type)
            }
    }
}