package com.douglassantana.sdui_components.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Icon Extensions
 *
 * PT: Converte o nome de ícone recebido do servidor em um [ImageVector] do Material.
 *     Preset fixo — nomes fora desta lista (ou nulos) retornam null, e nenhum botão
 *     de ícone é desenhado.
 *
 * EN: Converts the icon name received from the server into a Material [ImageVector].
 *     Fixed preset — names outside this list (or null) return null, and no icon
 *     button is drawn.
 *
 * Uso / Usage:
 *   val icon = "back".toIconVector()
 *
 * Valores aceitos / Accepted values:
 *   "back"   → Icons.AutoMirrored.Filled.ArrowBack
 *   "close"  → Icons.Filled.Close
 *   "menu"   → Icons.Filled.Menu
 *   "search" → Icons.Filled.Search
 *   "more"   → Icons.Filled.MoreVert
 */
fun String?.toIconVector(): ImageVector? = when (this) {
    "back" -> Icons.AutoMirrored.Filled.ArrowBack
    "close" -> Icons.Filled.Close
    "menu" -> Icons.Filled.Menu
    "search" -> Icons.Filled.Search
    "more" -> Icons.Filled.MoreVert
    else -> null
}
