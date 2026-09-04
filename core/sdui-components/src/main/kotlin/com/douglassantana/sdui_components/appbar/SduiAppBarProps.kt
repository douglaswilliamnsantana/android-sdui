package com.douglassantana.sdui_components.appbar

import com.douglassantana.sdui_core.IProps
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SduiAppBarProps(
    @SerialName("type") val type: String = "small",
    @SerialName("title") val title: String = "",
    @SerialName("leftIcon") val leftIcon: String? = null,
    @SerialName("leftAction") val leftAction: String? = null,
    @SerialName("rightIcon") val rightIcon: String? = null,
    @SerialName("rightAction") val rightAction: String? = null,
) : IProps
