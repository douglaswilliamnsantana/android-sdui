package com.douglassantana.sdui_core

import kotlinx.serialization.json.JsonObject

/**
 * Representa o contrato de dados recebido do servidor para um componente de UI.
 *
 * É o modelo cru do SDUI — agnóstico a qualquer framework de renderização.
 * Cada [Node] descreve um componente pelo seu [type] (ex: "text", "button"),
 * suas propriedades via [props] e seus filhos aninhados via [children].
 *
 * @property type Identificador do tipo do componente. Deve corresponder ao valor
 *   retornado por [ComponentFactory.type].
 * @property props Propriedades do componente mantidas como [JsonObject] para
 *   preservar o tipo e permitir deserialização segura nas factories.
 * @property children Lista de nós filhos. Processados recursivamente pelo
 *   [ComponentRegistry].
 *
 * ---
 *
 * Represents the data contract received from the server for a UI component.
 *
 * It is the raw SDUI model — agnostic to any rendering framework.
 * Each [Node] describes a component by its [type] (e.g. "text", "button"),
 * its properties via [props] and its nested children via [children].
 *
 * @property type Component type identifier. Must match the value
 *   returned by [ComponentFactory.type].
 * @property props Component properties held as [JsonObject] to
 *   preserve the type and allow safe deserialization in factories.
 * @property children List of child nodes. Processed recursively by
 *   [ComponentRegistry].
 */
data class Node(
    val type: String,
    val props: JsonObject = JsonObject(emptyMap()),
    val children: List<Node> = emptyList()
)