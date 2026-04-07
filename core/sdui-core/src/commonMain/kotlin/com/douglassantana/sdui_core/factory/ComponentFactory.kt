package com.douglassantana.sdui_core.factory

import com.douglassantana.sdui_core.IProps
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.context.SDUIContext
import kotlinx.serialization.json.Json

/**
 * Instância compartilhada de [Json] com configurações padrão para o SDUI.
 *
 * Shared [Json] instance with default settings for SDUI.
 */
val SduiJson: Json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

/**
 * ComponentFactory — Factory Genérica de Componentes SDUI
 *
 * PT: Factory genérica responsável por parsear um [Node] em props tipadas [P]
 *     e criar um [UIComponent] a partir delas.
 *     Implementações nunca lidam com JSON bruto ou casts de Map —
 *     recebem um [Node] limpo já mapeado pelo NodeMapper.
 *
 * EN: Generic factory responsible for parsing a [Node] into typed [P] props
 *     and creating a [UIComponent] from it.
 *     Implementations never deal with raw JSON or Map casts —
 *     they receive a clean [Node] already mapped by NodeMapper.
 *
 * Fluxo / Flow:
 *   NodeDto (Ktor) → NodeMapper → Node → parseProps() → create() → UIComponent
 *
 * Exemplo / Example:
 *   class SduiTextFactory : ComponentFactory<TextProps> {
 *       override fun type() = "text"
 *
 *       override fun parseProps(node: Node) = TextProps(
 *           text  = node.props["text"] as? String ?: "",
 *           style = node.style as? SduiTextStyle ?: SduiTextStyle()
 *       )
 *
 *       override fun create(props, context, children) = SduiText(
 *           value    = props.text,
 *           style    = props.style ?: SduiTextStyle(),
 *           children = children
 *       )
 *   }
 */
interface ComponentFactory<P : IProps> {

    /**
     * PT: Retorna o identificador de tipo que corresponde ao [Node.type] do servidor.
     *     ex: "text", "column", "button"
     *
     * EN: Returns the type identifier matching [Node.type] from the server.
     *     e.g. "text", "column", "button"
     */
    fun type(): String

    /**
     * PT: Parseia o [Node] limpo em props tipadas [P].
     *     Lê diretamente de [Node.props] e [Node.style] — sem casts necessários.
     *
     * EN: Parses the clean [Node] into typed [P] props.
     *     Read directly from [Node.props] and [Node.style] — no casts needed.
     *
     * @param node Nó limpo já mapeado pelo [NodeMapper]. / Clean node already mapped by [NodeMapper].
     */
    fun parseProps(node: Node): P

    /**
     * PT: Cria e retorna um [UIComponent] a partir das [props] tipadas.
     *     Recebe dados tipados prontos para uso — sem lógica de parsing aqui.
     *
     * EN: Creates and returns a [UIComponent] from the typed [props].
     *     Receives ready-to-use typed data — no parsing logic here.
     *
     * @param props Props tipadas parseadas do Node. / Typed props parsed from the Node.
     * @param context Contexto SDUI com informações de ambiente. / SDUI context carrying environment information.
     * @param children Filhos já resolvidos recursivamente pelo [ComponentRegistry]. / Children already recursively resolved by [ComponentRegistry].
     */
    fun create(
        props: P,
        context: SDUIContext,
        children: List<UIComponent> = emptyList()
    ): UIComponent

    /**
     * PT: Orquestra [parseProps] + [create]. Chamado pelo [ComponentRegistry].
     *     Sobrescreva apenas se precisar de lógica de orquestração customizada.
     *
     * EN: Orchestrates [parseProps] + [create]. Called by [ComponentRegistry].
     *     Override only if you need custom orchestration logic.
     */
    fun build(
        node: Node,
        context: SDUIContext,
        children: List<UIComponent> = emptyList()
    ): UIComponent = create(parseProps(node), context, children)
}