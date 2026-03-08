package com.douglassantana.sdui_core.action

/**
 * Representa uma ação de UI disparada por um componente SDUI.
 *
 * É uma sealed interface — todas as ações possíveis são subtipos conhecidos em tempo de compilação,
 * permitindo tratamento exaustivo com `when`. Novas ações devem ser adicionadas aqui e
 * tratadas na implementação de [ActionHandler].
 *
 * Uso típico: uma factory lê props do [Node]
 * (ex: `"navigate" to "/home"`) e cria a ação correspondente no [UIComponent].
 * O componente, ao ser interagido pelo usuário, chama
 * [SDUIContext.actionHandler]?.handle(action).
 *
 * ---
 *
 * Represents a UI action dispatched by an SDUI component.
 *
 * It is a sealed interface — all possible actions are known subtypes at compile time,
 * enabling exhaustive handling with `when`. New actions should be added here and
 * handled in the [ActionHandler] implementation.
 *
 * Typical usage: a factory reads props from a [com.douglassantana.android_sdui.sduiCore.Node]
 * (e.g. `"navigate" to "/home"`) and creates the corresponding action in the [UIComponent].
 * When the user interacts with the component, it calls
 * [SDUIContext.actionHandler]?.handle(action).
 */
sealed interface UIAction {
    data class Navigate(val route: String) : UIAction
    data class Log(val message: String) : UIAction
}