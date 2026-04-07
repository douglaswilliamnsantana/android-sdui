package com.douglassantana.shared

import com.douglassantana.sdui_core.Node
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Swift-friendly wrapper around [Node] for reading props without
 * dealing with raw [kotlinx.serialization.json.JsonElement] types.
 *
 * Usage in Swift:
 * ```swift
 * let reader = NodeReader(node: node)
 * let text   = reader.stringProp("text")     // "Hello SDUI"
 * let size   = reader.doubleProp("fontSize") // 22.0
 * ```
 */
class NodeReader(val node: Node) {

    val type: String get() = node.type

    val children: List<NodeReader>
        get() = node.children.map { NodeReader(it) }

    fun stringProp(key: String): String? =
        (node.props[key] as? JsonPrimitive)?.takeIf { it.isString }?.content

    fun doubleProp(key: String): Double? =
        (node.props[key] as? JsonPrimitive)?.doubleOrNull

    fun boolProp(key: String): Boolean? =
        (node.props[key] as? JsonPrimitive)?.booleanOrNull

    /** Read a nested object prop and return a sub-reader for it. */
    fun objectProp(key: String): NodeReader? {
        val obj = node.props[key] as? JsonObject ?: return null
        return NodeReader(Node(type = key, props = obj))
    }
}
