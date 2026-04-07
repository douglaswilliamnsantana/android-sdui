package com.douglassantana.shared.theme

import com.douglassantana.shared.theme.SduiColor.Companion.fromArgbHex

/**
 * Design tokens mirroring core/designsystem Color.kt / ColorsLight.kt / ColorsDark.kt.
 * These are the source of truth for iOS; Android reads them from Compose directly.
 */
object SduiColorTokens {

    object Light {
        val primary = fromArgbHex(0xFF2A3F5F)
        val onPrimary = fromArgbHex(0xFFFFFFFF)
        val background = fromArgbHex(0xFFF7F8F9)
        val onBackground = fromArgbHex(0xFF1A202C)
        val surface = fromArgbHex(0xFFFFFFFF)
        val onSurface = fromArgbHex(0xFF4A5568)
        val outline = fromArgbHex(0xFFEAECEF)
        val outlineVariant = fromArgbHex(0xFFC4C9D4)
        val error = fromArgbHex(0xFFC0392B)
        val errorContainer = fromArgbHex(0xFFFADED8)
    }

    object Dark {
        val primary = fromArgbHex(0xFF8F9BB3)
        val onPrimary = fromArgbHex(0xFF0F1523)
        val background = fromArgbHex(0xFF0F1523)
        val onBackground = fromArgbHex(0xFFEAECEF)
        val surface = fromArgbHex(0xFF1A2236)
        val onSurface = fromArgbHex(0xFF8E96A8)
        val outline = fromArgbHex(0xFF2A3347)
        val outlineVariant = fromArgbHex(0xFF3D4F6B)
        val error = fromArgbHex(0xFFE74C3C)
        val errorContainer = fromArgbHex(0xFF4A1C18)
    }
}
