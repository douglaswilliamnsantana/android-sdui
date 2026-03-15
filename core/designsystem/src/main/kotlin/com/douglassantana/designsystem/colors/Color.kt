package com.douglassantana.designsystem.colors

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)

val LightColorScheme = lightColorScheme(
    primary = Primary500,
    onPrimary = White,
    background = Neutral50,
    onBackground = Neutral900,
    surface = White,
    onSurface = Neutral600,
    outline = Neutral100,
    outlineVariant = Neutral200,
    error = ErrorLight,
    errorContainer = ErrorContainerLight,
)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark400,
    onPrimary = NeutralDark50,
    background = NeutralDark50,
    onBackground = NeutralDark900,
    surface = NeutralDark100,
    onSurface = NeutralDark600,
    outline = NeutralDark200,
    outlineVariant = NeutralDark400,
    error = ErrorDark,
    errorContainer = ErrorContainerDark,
)