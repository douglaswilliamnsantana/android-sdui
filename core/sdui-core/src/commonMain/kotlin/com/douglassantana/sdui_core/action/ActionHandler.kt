package com.douglassantana.sdui_core.action

/**
 * Contrato para tratamento de ações disparadas por componentes SDUI.
 *
 * Implementações concretas devem ser fornecidas pela camada de aplicação (ex: Activity ou
 * ViewModel) e injetadas no [SDUIContext].
 * Dessa forma, o core SDUI permanece desacoplado de navegação, analytics ou qualquer
 * efeito colateral específico da plataforma.
 *
 * Exemplo de implementação:
 * ```kotlin
 * class AppActionHandler(private val navController: NavController) : ActionHandler {
 *     override fun handle(action: UIAction) = when (action) {
 *         is UIAction.Navigate -> navController.navigate(action.route)
 *         is UIAction.Log     -> android.util.Log.d("SDUI", action.message)
 *     }
 * }
 * ```
 *
 * ---
 *
 * Contract for handling actions dispatched by SDUI components.
 *
 * Concrete implementations should be provided by the application layer (e.g. Activity or
 * ViewModel) and injected into [SDUIContext].
 * This keeps the SDUI core decoupled from navigation, analytics, or any
 * platform-specific side effects.
 *
 * Implementation example:
 * ```kotlin
 * class AppActionHandler(private val navController: NavController) : ActionHandler {
 *     override fun handle(action: UIAction) = when (action) {
 *         is UIAction.Navigate -> navController.navigate(action.route)
 *         is UIAction.Log     -> android.util.Log.d("SDUI", action.message)
 *     }
 * }
 * ```
 */
interface ActionHandler {
    fun handle(action: UIAction)
}