package com.douglassantana.sdui_components.appbar

import com.douglassantana.sdui_core.UIComponent

/**
 * PT: [type] e os nomes de ícone ficam como String — a conversão para os tipos
 *     concretos do Compose ([com.douglassantana.designsystem.components.appbar.AppBarType],
 *     [androidx.compose.ui.graphics.vector.ImageVector]) acontece só no renderer, seguindo
 *     o mesmo padrão de [com.douglassantana.sdui_components.text.SduiTextStyle].
 *
 * EN: [type] and the icon names stay as String — conversion to concrete Compose types
 *     ([com.douglassantana.designsystem.components.appbar.AppBarType],
 *     [androidx.compose.ui.graphics.vector.ImageVector]) only happens in the renderer,
 *     following the same pattern as [com.douglassantana.sdui_components.text.SduiTextStyle].
 */
data class SduiAppBar(
    val type: String,
    val title: String,
    val leftIcon: String?,
    val leftIconAction: (() -> Unit)?,
    val rightIcon: String?,
    val rightIconAction: (() -> Unit)?,
) : UIComponent
