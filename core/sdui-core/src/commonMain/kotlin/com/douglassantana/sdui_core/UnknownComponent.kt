package com.douglassantana.sdui_core

/**
 * Componente de fallback usado quando nenhuma factory está registrada para um [Node.type].
 *
 * Criado automaticamente pelo [ComponentRegistry]
 * quando o tipo do [Node] não possui correspondência no mapa de factories.
 * O [RendererRegistry] não possui
 * renderer para este tipo, portanto nada é renderizado — mas um aviso é emitido via Log.
 *
 * @property type O tipo desconhecido recebido do servidor, preservado para fins de diagnóstico.
 *
 * ---
 *
 * Fallback component used when no factory is registered for a given [Node.type].
 *
 * Automatically created by [ComponentRegistry]
 * when the [Node] type has no match in the factory map.
 * The [RendererRegistry] has no
 * renderer for this type, so nothing is rendered — but a warning is emitted via Log.
 *
 * @property type The unknown type received from the server, preserved for diagnostic purposes.
 */
data class UnknownComponent(val type: String) : UIComponent