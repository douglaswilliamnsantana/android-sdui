package com.douglassantana.sdui_components.screen

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.ComponentFactory

/**
 * PT: [children] chega aqui já resolvido pelo ComponentRegistry — cada nó filho do JSON
 *     virou um [SduiHeader]/[SduiBody]/[SduiBottom] via sua própria factory (registradas
 *     com type "header"/"body"/"bottom"). Aqui só resta escolher cada slot pelo tipo
 *     concreto; a ordem deles no JSON é irrelevante.
 *
 * EN: [children] arrives here already resolved by ComponentRegistry — each JSON child
 *     node became a [SduiHeader]/[SduiBody]/[SduiBottom] through its own factory
 *     (registered under type "header"/"body"/"bottom"). All that's left is picking each
 *     slot by its concrete type; their order in the JSON doesn't matter.
 */
class SduiScreenFactory : ComponentFactory<SduiScreenProps> {

    override fun type() = "screen"

    override fun parseProps(node: Node) = SduiScreenProps

    override fun create(
        props: SduiScreenProps,
        context: SDUIContext,
        children: List<UIComponent>
    ) = SduiScreen(
        header = children.filterIsInstance<SduiHeader>().firstOrNull(),
        body = children.filterIsInstance<SduiBody>().firstOrNull(),
        bottom = children.filterIsInstance<SduiBottom>().firstOrNull(),
    )
}
