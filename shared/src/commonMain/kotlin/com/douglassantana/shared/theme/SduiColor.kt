package com.douglassantana.shared.theme

/**
 * Platform-agnostic color representation.
 * Components are in [0.0, 1.0] range.
 *
 * Android → mapped to androidx.compose.ui.graphics.Color
 * iOS     → mapped to SwiftUI.Color via SduiTheme.swift
 */
data class SduiColor(
    val alpha: Double,
    val red: Double,
    val green: Double,
    val blue: Double,
) {
    companion object {
        /** Parse a 0xAARRGGBB hex literal (as used in Kotlin Color declarations). */
        fun fromArgbHex(hex: Long): SduiColor {
            val value = hex and 0xFFFFFFFFL
            return SduiColor(
                alpha = ((value shr 24) and 0xFF).toDouble() / 255.0,
                red = ((value shr 16) and 0xFF).toDouble() / 255.0,
                green = ((value shr 8) and 0xFF).toDouble() / 255.0,
                blue = (value and 0xFF).toDouble() / 255.0,
            )
        }
    }
}
