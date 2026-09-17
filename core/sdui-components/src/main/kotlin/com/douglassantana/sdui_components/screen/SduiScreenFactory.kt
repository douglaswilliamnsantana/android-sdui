package com.douglassantana.sdui_components.screen

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.SduiNodeType
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.ComponentFactory

/**
 * PT: [children] chega aqui já resolvido pelo ComponentRegistry — cada nó filho do JSON
 *     virou um [SduiHeader]/[SduiBody]/[SduiBottom] via sua própria factory (registradas
 *     com type "header"/"body"/"bottom"). Aqui só resta escolher cada slot pelo tipo
 *     concreto; a ordem deles no JSON é irrelevante.
 *
 *     O `when` sobre [SduiScreenSlot] (sealed interface) é exaustivo: se um novo slot for
 *     adicionado sem alterar esta factory, o build quebra aqui em vez de o slot ser
 *     silenciosamente descartado — ao contrário de uma cadeia de `filterIsInstance`, que
 *     não avisa quando falta um `filterIsInstance<NovoSlot>()`.
 *
 * EN: [children] arrives here already resolved by ComponentRegistry — each JSON child
 *     node became a [SduiHeader]/[SduiBody]/[SduiBottom] through its own factory
 *     (registered under type "header"/"body"/"bottom"). All that's left is picking each
 *     slot by its concrete type; their order in the JSON doesn't matter.
 *
 *     The `when` over [SduiScreenSlot] (sealed interface) is exhaustive: if a new slot is
 *     added without touching this factory, the build breaks here instead of the slot being
 *     silently dropped — unlike a `filterIsInstance` chain, which gives no warning when a
 *     `filterIsInstance<NewSlot>()` line is missing.
 */
class SduiScreenFactory : ComponentFactory<SduiScreenProps> {

    override fun type() = SduiNodeType.SCREEN

    override fun parseProps(node: Node) = SduiScreenProps

    override fun create(
        props: SduiScreenProps,
        context: SDUIContext,
        children: List<UIComponent>
    ): SduiScreen {
        var header: SduiHeader? = null
        var body: SduiBody? = null
        var bottom: SduiBottom? = null

        children.filterIsInstance<SduiScreenSlot>().forEach { slot ->
            when (slot) {
                is SduiHeader -> header = slot
                is SduiBody -> body = slot
                is SduiBottom -> bottom = slot
            }
        }

        return SduiScreen(header = header, body = body, bottom = bottom)
    }
}
