package com.douglassantana.sdui_components.text

import com.douglassantana.sdui_core.UIComponent

data class SduiText(
    val value: String,
    val style: SduiTextStyle = SduiTextStyle(),
    override val children: List<UIComponent> = emptyList(),
) : UIComponent