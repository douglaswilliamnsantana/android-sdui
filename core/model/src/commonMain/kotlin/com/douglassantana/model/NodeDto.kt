package com.douglassantana.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class NodeDto(
    @SerialName("type") val type: String = "",
    @SerialName("props") val props: Map<String, JsonElement> = emptyMap(),
    @SerialName("components") val components: List<NodeDto> = emptyList(),
)
