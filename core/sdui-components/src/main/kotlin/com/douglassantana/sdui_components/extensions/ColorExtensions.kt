package com.douglassantana.sdui_components.extensions

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Color Extensions
 *
 * PT: Funções de extensão para converter tokens de cor do SDUI em [Color] do Compose.
 *     Usadas por todos os renderers para evitar duplicação de código.
 *
 * EN: Extension functions to convert SDUI color tokens into Compose [Color].
 *     Used by all component renderers to avoid code duplication.
 *
 * Uso / Usage:
 *   val color = "#1A202C".toColor()
 *   val color = "#1A202C".toColorOrDefault(Color.Black)
 *   val color = nullableString.toColorOrUnspecified()
 */

// ─── String ───────────────────────────────────────────────

/**
 * PT: Converte uma string de cor hex em um [Color] do Compose.
 *     Retorna [Color.Unspecified] se a string for nula ou inválida.
 *
 * EN: Converts a hex color string to a Compose [Color].
 *     Returns [Color.Unspecified] if the string is null or invalid.
 */
fun String?.toColor(): Color =
    this?.let {
        runCatching {
            Color(it.toColorInt())
        }.getOrElse { Color.Unspecified }
    } ?: Color.Unspecified

/**
 * PT: Converte uma string de cor hex em um [Color] do Compose.
 *     Retorna a cor [default] se a string for nula ou inválida.
 *
 * EN: Converts a hex color string to a Compose [Color].
 *     Returns the [default] color if the string is null or invalid.
 */
fun String?.toColorOrDefault(default: Color): Color =
    this?.let {
        runCatching {
            Color(it.toColorInt())
        }.getOrElse { default }
    } ?: default

/**
 * PT: Converte uma string de cor hex em um [Color] do Compose.
 *     Retorna [Color.Transparent] se a string for nula ou inválida.
 *
 * EN: Converts a hex color string to a Compose [Color].
 *     Returns [Color.Transparent] if the string is null or invalid.
 */
fun String?.toColorOrTransparent(): Color =
    toColorOrDefault(Color.Transparent)