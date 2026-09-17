package com.douglassantana.sdui_core.log

/**
 * Abstração de logging usada pelo core SDUI (ex: [com.douglassantana.sdui_core.registry.ComponentRegistry]).
 *
 * Mantém o core desacoplado de uma API de logging de plataforma específica
 * (ex: `android.util.Log`), permitindo trocar a implementação por Koin e
 * testar sem depender do Android SDK.
 *
 * ---
 *
 * Logging abstraction used by the SDUI core (e.g. [com.douglassantana.sdui_core.registry.ComponentRegistry]).
 *
 * Keeps the core decoupled from a platform-specific logging API
 * (e.g. `android.util.Log`), allowing the implementation to be swapped via Koin
 * and tested without depending on the Android SDK.
 */
interface SduiLogger {
    fun warn(tag: String, message: String)
}
