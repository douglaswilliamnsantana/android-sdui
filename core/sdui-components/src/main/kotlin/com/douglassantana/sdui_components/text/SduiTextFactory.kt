package com.douglassantana.sdui_components.text

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.ComponentFactory
import com.douglassantana.sdui_core.factory.SduiJson
import kotlinx.serialization.json.decodeFromJsonElement
import javax.inject.Inject

class SduiTextFactory @Inject constructor() : ComponentFactory<SduiTextProps> {

    override fun type() = "text"

    override fun parseProps(node: Node): SduiTextProps =
        SduiJson.decodeFromJsonElement(node.props)

    override fun create(
        props: SduiTextProps,
        context: SDUIContext,
        children: List<UIComponent>
    ) = SduiText(
        value = props.text,
        style = props.style ?: SduiTextStyle(),
        children = children
    )
}