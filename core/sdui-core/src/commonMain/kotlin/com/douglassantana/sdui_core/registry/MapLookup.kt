package com.douglassantana.sdui_core.registry

import com.douglassantana.sdui_core.log.SduiLogger

/**
 * PT: Busca [key] neste mapa; se ausente, registra um aviso via [logger] (com [tag] e a
 *     mensagem de [onMissing]) e retorna `null` em vez de lançar.
 *
 *     Compartilhado por `ComponentRegistry` e `RendererRegistry` — ambos resolvem "não há
 *     entrada pra essa chave" da mesma forma: log + fallback silencioso, nunca uma exceção.
 *
 * EN: Looks up [key] in this map; if absent, logs a warning via [logger] (with [tag] and
 *     [onMissing]'s message) and returns `null` instead of throwing.
 *
 *     Shared by `ComponentRegistry` and `RendererRegistry` — both resolve "no entry for this
 *     key" the same way: log + silent fallback, never an exception.
 */
fun <K, V> Map<K, V>.lookupOrWarn(
    key: K,
    logger: SduiLogger,
    tag: String,
    onMissing: (K) -> String,
): V? = this[key] ?: run {
    logger.warn(tag, onMissing(key))
    null
}
