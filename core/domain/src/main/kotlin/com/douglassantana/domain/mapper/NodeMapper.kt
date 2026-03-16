package com.douglassantana.domain.mapper

import com.douglassantana.domain.model.NodeDto
import com.douglassantana.sdui_core.Node
import kotlinx.serialization.json.JsonObject

object NodeMapper {
    fun toNode(dto: NodeDto): Node = Node(
        type = dto.type,
        props = JsonObject(dto.props),
        children = dto.components.map { toNode(it) }
    )
}