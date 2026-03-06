package com.douglassantana.android_sdui.sduiCore

/**
 * Representa o contrato de dados recebido do servidor para um componente de UI.
 *
 * É o modelo cru do SDUI — agnóstico a qualquer framework de renderização.
 * Cada [Node] descreve um componente pelo seu [type] (ex: "text", "button"),
 * suas propriedades via [props] (ex: `"label" to "Clique aqui"`) e
 * seus filhos aninhados via [children], permitindo árvores de componentes.
 *
 * @property type Identificador do tipo do componente. Deve corresponder ao valor
 *   retornado por [com.douglassantana.android_sdui.sduiCore.factory.ComponentFactory.type].
 * @property props Mapa de propriedades arbitrárias do componente (ex: texto, cor, rota).
 * @property children Lista de nós filhos. Processados recursivamente pelo
 *   [com.douglassantana.android_sdui.sduiCore.registry.ComponentRegistry].
 *
 * ---
 *
 * Represents the data contract received from the server for a UI component.
 *
 * This is the raw SDUI model — agnostic to any rendering framework.
 * Each [Node] describes a component by its [type] (e.g. "text", "button"),
 * its properties via [props] (e.g. `"label" to "Click here"`), and
 * its nested children via [children], allowing component trees.
 *
 * @property type Component type identifier. Must match the value returned by
 *   [com.douglassantana.android_sdui.sduiCore.factory.ComponentFactory.type].
 * @property props Map of arbitrary component properties (e.g. text, color, route).
 * @property children List of child nodes. Processed recursively by
 *   [com.douglassantana.android_sdui.sduiCore.registry.ComponentRegistry].
 */
data class Node(
    val type: String,
    val props: Map<String, Any?> = emptyMap(),
    val children: List<Node> = emptyList()
)