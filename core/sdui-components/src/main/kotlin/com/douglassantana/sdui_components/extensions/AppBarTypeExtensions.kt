package com.douglassantana.sdui_components.extensions

import com.douglassantana.designsystem.components.appbar.AppBarType

/**
 * AppBarType Extensions
 *
 * PT: Função de extensão para converter o tipo de app bar recebido do servidor
 *     em [AppBarType] do design system.
 *
 * EN: Extension function to convert the app bar type received from the server
 *     into the design system's [AppBarType].
 *
 * Uso / Usage:
 *   val type = "medium".toAppBarType()
 *
 * Valores aceitos / Accepted values:
 *   "small"          → AppBarType.Small
 *   "center-aligned" → AppBarType.CenterAligned
 *   "medium"         → AppBarType.Medium
 *   "large"          → AppBarType.Large
 */
fun String.toAppBarType(): AppBarType = when (this) {
    "center-aligned" -> AppBarType.CenterAligned
    "medium" -> AppBarType.Medium
    "large" -> AppBarType.Large
    else -> AppBarType.Small
}
