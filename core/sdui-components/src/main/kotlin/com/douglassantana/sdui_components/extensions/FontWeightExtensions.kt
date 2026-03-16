package com.douglassantana.sdui_components.extensions

import androidx.compose.ui.text.font.FontWeight

/**
 * FontWeight Extensions
 *
 * PT: Funções de extensão para converter tokens de peso de fonte do SDUI
 *     em [FontWeight] do Compose.
 *
 * EN: Extension functions to convert SDUI font weight tokens
 *     into Compose [FontWeight].
 *
 * Uso / Usage:
 *   val fontWeight = "semi-bold".toFontWeight()
 *   val fontWeight = "bold".toFontWeight()
 *
 * Valores aceitos / Accepted values:
 *   "thin"      → FontWeight.Thin      (100)
 *   "light"     → FontWeight.Light     (300)
 *   "normal"    → FontWeight.Normal    (400)
 *   "medium"    → FontWeight.Medium    (500)
 *   "semi-bold" → FontWeight.SemiBold  (600)
 *   "bold"      → FontWeight.Bold      (700)
 */
fun String.toFontWeight(): FontWeight = when (this) {
    "thin" -> FontWeight.Thin
    "light" -> FontWeight.Light
    "medium" -> FontWeight.Medium
    "semi-bold" -> FontWeight.SemiBold
    "bold" -> FontWeight.Bold
    else -> FontWeight.Normal
}