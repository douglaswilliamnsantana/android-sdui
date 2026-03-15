package com.douglassantana.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.douglassantana.designsystem.colors.DarkColorScheme
import com.douglassantana.designsystem.colors.LightColorScheme
import com.douglassantana.designsystem.typography.Typography

@Composable
fun AndroidSduiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}