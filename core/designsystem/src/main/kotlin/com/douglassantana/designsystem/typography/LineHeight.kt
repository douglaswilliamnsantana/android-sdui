package com.douglassantana.designsystem.typography

import androidx.compose.ui.unit.sp

/**
 * Design System — Line Height Tokens
 * Tokens de Altura de Linha do Design System
 *
 * EN: Centralizes all line height values used across the app,
 *     ensuring consistent vertical rhythm and readability
 *     throughout the typography scale.
 *
 * PT: Centraliza todos os valores de altura de linha utilizados no app,
 *     garantindo ritmo vertical consistente e legibilidade
 *     em toda a escala tipográfica.
 *
 * Usage / Uso:
 *   lineHeight = LineHeight.BodyLarge
 *   lineHeight = LineHeight.TitleMedium
 */
object LineHeight {
    val DisplayLarge = 64.sp
    val DisplayMedium = 52.sp
    val DisplaySmall = 44.sp
    val HeadlineLarge = 40.sp
    val HeadlineMedium = 36.sp
    val HeadlineSmall = 32.sp
    val TitleLarge = 28.sp
    val TitleMedium = 24.sp
    val TitleSmall = 20.sp
    val BodyLarge = 24.sp
    val BodyMedium = 20.sp
    val BodySmall = 16.sp
    val LabelLarge = 20.sp
    val LabelMedium = 16.sp
    val LabelSmall = 16.sp
}