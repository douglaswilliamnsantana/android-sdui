package com.douglassantana.sdui_components.extensions

import androidx.compose.ui.text.font.FontStyle

/**
 * FontStyle Extensions
 *
 * PT: Funções de extensão para converter tokens de estilo de fonte do SDUI
 *     em [FontStyle] do Compose.
 *
 * EN: Extension functions to convert SDUI font style tokens
 *     into Compose [FontStyle].
 *
 * Uso / Usage:
 *   val fontStyle = "italic".toFontStyle()
 *   val fontStyle = "normal".toFontStyle()
 *
 * Valores aceitos / Accepted values:
 *   "normal" → FontStyle.Normal
 *   "italic" → FontStyle.Italic
 */
fun String.toFontStyle(): FontStyle = when (this) {
    "italic" -> FontStyle.Italic
    else -> FontStyle.Normal
}