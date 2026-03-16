package com.douglassantana.sdui_components.text

import com.douglassantana.sdui_core.IProps
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SduiTextProps(
    @SerialName("text") val text: String = "",
    @SerialName("style") val style: SduiTextStyle? = null,
) : IProps