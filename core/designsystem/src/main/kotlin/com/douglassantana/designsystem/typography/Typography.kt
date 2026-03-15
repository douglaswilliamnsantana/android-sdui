package com.douglassantana.designsystem.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Design System — Typography
 * Tipografia do Design System
 *
 * EN: Defines the typography scale using Inter font family loaded locally,
 *     ensuring consistent rendering across all devices and environments,
 *     regardless of internet connectivity.
 *
 * PT: Define a escala tipográfica usando a família de fontes Inter carregada localmente,
 *     garantindo renderização consistente em todos os dispositivos e ambientes,
 *     independente de conectividade com a internet.
 *
 * Usage / Uso:
 *   Text(text = "Title", style = MaterialTheme.typography.titleLarge)
 *   MaterialTheme(typography = Typography)
 */

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSize.DisplayLarge,
        lineHeight = LineHeight.DisplayLarge
    ),
    displayMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSize.DisplayMedium,
        lineHeight = LineHeight.DisplayMedium
    ),
    displaySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = FontSize.DisplaySmall,
        lineHeight = LineHeight.DisplaySmall
    ),
    headlineLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.HeadlineLarge,
        lineHeight = LineHeight.HeadlineLarge
    ),
    headlineMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.HeadlineMedium,
        lineHeight = LineHeight.HeadlineMedium
    ),
    headlineSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.HeadlineSmall,
        lineHeight = LineHeight.HeadlineSmall
    ),
    titleLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = FontSize.TitleLarge,
        lineHeight = LineHeight.TitleLarge
    ),
    titleMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.TitleMedium,
        lineHeight = LineHeight.TitleMedium
    ),
    titleSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.TitleSmall,
        lineHeight = LineHeight.TitleSmall
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.BodyLarge,
        lineHeight = LineHeight.BodyLarge,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.BodyMedium,
        lineHeight = LineHeight.BodyMedium
    ),
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = FontSize.BodySmall,
        lineHeight = LineHeight.BodySmall
    ),
    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.LabelLarge,
        lineHeight = LineHeight.LabelLarge
    ),
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.LabelMedium,
        lineHeight = LineHeight.LabelMedium
    ),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = FontSize.LabelSmall,
        lineHeight = LineHeight.LabelSmall
    )
)
