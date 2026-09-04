package com.douglassantana.sdui_components.screen

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.ComponentFactory

class SduiHeaderFactory : ComponentFactory<SduiScreenSlotProps> {

    override fun type() = "header"

    override fun parseProps(node: Node) = SduiScreenSlotProps

    override fun create(
        props: SduiScreenSlotProps,
        context: SDUIContext,
        children: List<UIComponent>
    ) = SduiHeader(children = children)
}
