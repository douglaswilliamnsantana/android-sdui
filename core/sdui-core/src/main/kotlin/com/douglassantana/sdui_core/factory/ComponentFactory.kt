package com.douglassantana.sdui_core.factory

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.context.SDUIContext

/**
 * Contrato para criação de [UIComponent] a partir de um [Node].
 *
 * Cada implementação é responsável por um único tipo de componente, identificado por [type].
 * O [ComponentRegistry] usa esse valor
 * para rotear a criação ao factory correto.
 *
 * As implementações devem ser registradas no módulo Hilt com `@Binds @IntoSet` para que
 * sejam descobertas automaticamente pelo [ComponentRegistry].
 *
 * @see HomeTextFactory
 *
 * ---
 *
 * Contract for creating a [UIComponent] from a [Node].
 *
 * Each implementation handles a single component type, identified by [type].
 * The [ComponentRegistry] uses this value
 * to route creation to the correct factory.
 *
 * Implementations must be registered in the Hilt module with `@Binds @IntoSet` so they are
 * automatically discovered by the [ComponentRegistry].
 *
 * @see HomeTextFactory
 */
interface ComponentFactory {
    /**
     * Retorna o identificador do tipo de componente que esta factory sabe criar.
     * Deve corresponder ao valor de [Node.type] vindo do servidor (ex: "text", "button").
     *
     * Returns the type identifier of the component this factory knows how to create.
     * Must match the [Node.type] value from the server (e.g. "text", "button").
     */
    fun type(): String

    /**
     * Cria e retorna um [UIComponent] a partir dos dados do [node].
     *
     * @param node O nó recebido do servidor com tipo, props e filhos brutos.
     * @param context Contexto SDUI com informações de ambiente (locale, actionHandler, extras).
     * @param children Filhos já resolvidos recursivamente pelo [ComponentRegistry].
     *
     * ---
     *
     * Creates and returns a [UIComponent] from the [node] data.
     *
     * @param node The node received from the server with type, props and raw children.
     * @param context SDUI context carrying environment information (locale, actionHandler, extras).
     * @param children Children already recursively resolved by [ComponentRegistry].
     */
    fun create(
        node: Node,
        context: SDUIContext,
        children: List<UIComponent> = emptyList()
    ): UIComponent
}