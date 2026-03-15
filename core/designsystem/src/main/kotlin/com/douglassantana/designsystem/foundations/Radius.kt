package com.douglassantana.designsystem.foundations

import androidx.compose.ui.unit.dp

/**
 * Design System — Radius Tokens
 * Tokens de Raio de Borda do Design System
 *
 * EN: Centralizes all corner radius values used across the app,
 *     ensuring visual consistency and making it easier to maintain
 *     the rounding standards of components.
 *
 * PT: Centraliza todos os valores de raio de borda utilizados no app,
 *     garantindo consistência visual e facilitando a manutenção dos
 *     padrões de arredondamento dos componentes.
 *
 * Usage / Uso:
 *   RoundedCornerShape(Radius.Medium)
 *   Modifier.clip(RoundedCornerShape(Radius.Large))
 */
object Radius {
    val RadiusNone = 0.dp
    val RadiusXSmall = 2.dp
    val RadiusSmall = 4.dp
    val RadiusMedium = 8.dp
    val RadiusLarge = 16.dp
    val RadiusXLarge = 24.dp
    val RadiusFull = 50.dp
}