package com.douglassantana.sdui_components.text

import com.douglassantana.domain.style.IMargin
import com.douglassantana.domain.style.IStyle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SduiTextStyle(
    @SerialName("padding") override val padding: IMargin = IMargin(),
    @SerialName("color") val color: String? = null,
    @SerialName("fontSize") val fontSize: Int? = null,
    @SerialName("fontWeight") val fontWeight: String = "normal",
    @SerialName("fontStyle") val fontStyle: String = "normal",
    @SerialName("fontFamily") val fontFamily: String = "inter",
    @SerialName("maxLines") val maxLines: Int = Int.MAX_VALUE,
    @SerialName("minLines") val minLines: Int = 1,
) : IStyle()