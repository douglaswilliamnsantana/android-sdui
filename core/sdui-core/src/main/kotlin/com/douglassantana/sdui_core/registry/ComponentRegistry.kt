package com.douglassantana.sdui_core.registry

import android.util.Log
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.UnknownComponent
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.ComponentFactory
import javax.inject.Inject

/**
 * Registro central de factories de componentes SDUI.
 *
 * Recebe via injeção (Hilt multibindings) o conjunto de todas as [ComponentFactory]
 * registradas no grafo e os organiza em um mapa indexado por [ComponentFactory.type].
 *
 * Ao receber um [Node], resolve a factory correspondente e delega a criação do [UIComponent].
 * Os filhos do [Node] são processados recursivamente antes da criação do componente pai,
 * garantindo que a árvore completa de [UIComponent] seja construída de cima para baixo.
 *
 * Se nenhuma factory for encontrada para o tipo do [Node], emite um aviso via [Log.w]
 * e retorna [UnknownComponent] como fallback — sem crashar a aplicação.
 *
 * ---
 *
 * Central registry for SDUI component factories.
 *
 * Receives via injection (Hilt multibindings) the full set of registered [ComponentFactory]
 * instances and organizes them in a map indexed by [ComponentFactory.type].
 *
 * When given a [Node], it resolves the matching factory and delegates [UIComponent] creation.
 * Child nodes are resolved recursively before creating the parent component,
 * ensuring the complete [UIComponent] tree is built top-down.
 *
 * If no factory is found for the [Node] type, a warning is emitted via [Log.w]
 * and [UnknownComponent] is returned as a fallback — without crashing the app.
 */
class ComponentRegistry @Inject constructor(
    factories: Set<@JvmSuppressWildcards ComponentFactory>
) {

    private val factoryMap = factories.associateBy { it.type() }

    fun create(
        node: Node,
        context: SDUIContext
    ): UIComponent {
        val resolvedChildren = node.children.map { create(it, context) }

        return factoryMap[node.type]
            ?.create(node, context, resolvedChildren)
            ?: run {
                Log.w("ComponentRegistry", "No factory registered for type '${node.type}'. Falling back to UnknownComponent.")
                UnknownComponent(node.type)
            }
    }
}