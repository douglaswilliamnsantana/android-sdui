package com.douglassantana.sdui_core.context

import com.douglassantana.sdui_core.action.ActionHandler

/**
 * Contexto propagado a todas as factories durante a criação de componentes SDUI.
 *
 * @property actionHandler Handler opcional para despachar [UIAction].
 * @property languageTag BCP 47 language tag (ex: "pt-BR", "en-US"). Padrão: "en".
 * @property extras Mapa de dados extras de contexto (feature flags, user id, etc).
 */
data class SDUIContext(
    val actionHandler: ActionHandler? = null,
    val languageTag: String = "en",
    val extras: Map<String, Any?> = emptyMap()
)
