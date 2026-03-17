package com.douglassantana.model.style

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IMargin(
    @SerialName("start") val start: Int = 0,
    @SerialName("end") val end: Int = 0,
    @SerialName("top") val top: Int = 0,
    @SerialName("bottom") val bottom: Int = 0,
)