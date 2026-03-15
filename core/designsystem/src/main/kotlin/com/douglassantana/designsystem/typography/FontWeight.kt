package com.douglassantana.designsystem.typography

import androidx.compose.ui.text.font.FontWeight

/**
 * Design System — Font Weight Tokens
 * Tokens de Peso de Fonte do Design System
 *
 * EN: Centralizes all font weight values used across the app,
 *     ensuring typographic consistency and making it easier
 *     to maintain the text weight standards.
 *
 * PT: Centraliza todos os valores de peso de fonte utilizados no app,
 *     garantindo consistência tipográfica e facilitando a manutenção
 *     dos padrões de peso de texto.
 *
 * Usage / Uso:
 *   fontWeight = AppFontWeight.Bold
 *   fontWeight = AppFontWeight.Medium
 */
object FontWeight {
    val Light = FontWeight.Light
    val Normal = FontWeight.Normal
    val Medium = FontWeight.Medium
    val SemiBold = FontWeight.SemiBold
    val Bold = FontWeight.Bold
}