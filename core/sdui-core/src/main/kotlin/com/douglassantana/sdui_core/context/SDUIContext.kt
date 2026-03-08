package com.douglassantana.sdui_core.context

import com.douglassantana.sdui_core.action.ActionHandler
import java.util.Locale

/**
 * Contexto propagado a todas as factories durante a criação de componentes SDUI.
 *
 * Funciona como um "envelope de ambiente" — carrega informações globais que as factories
 * podem precisar para instanciar seus componentes corretamente, sem depender de singletons
 * ou variáveis globais.
 *
 * @property actionHandler Handler opcional para despachar [UIAction].
 *   Deve ser fornecido pela camada de aplicação (Activity/ViewModel). Se nulo, ações são ignoradas.
 * @property locale Locale atual do dispositivo. Útil para factories que precisam adaptar
 *   conteúdo a idiomas ou formatos regionais. Padrão: [Locale.getDefault].
 * @property extras Mapa de dados extras de contexto para casos específicos, como feature flags,
 *   identificadores de usuário ou parâmetros de tela. Padrão: mapa vazio.
 *
 * ---
 *
 * Context propagated to all factories during SDUI component creation.
 *
 * Acts as an "environment envelope" — carries global information that factories
 * may need to instantiate their components correctly, without relying on singletons
 * or global variables.
 *
 * @property actionHandler Optional handler for dispatching [com.douglassantana.android_sdui.sduiCore.action.UIAction].
 *   Should be provided by the application layer (Activity/ViewModel). If null, actions are ignored.
 * @property locale Current device locale. Useful for factories that need to adapt
 *   content to languages or regional formats. Default: [Locale.getDefault].
 * @property extras Map of extra context data for specific cases, such as feature flags,
 *   user identifiers, or screen parameters. Default: empty map.
 */
data class SDUIContext(
    val actionHandler: ActionHandler? = null,
    val locale: Locale = Locale.getDefault(),
    val extras: Map<String, Any?> = emptyMap()
)