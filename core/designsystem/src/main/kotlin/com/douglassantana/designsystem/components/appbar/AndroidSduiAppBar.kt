package com.douglassantana.designsystem.components.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * PT: AppBar do design system com os quatro tipos do Material 3 (ver [AppBarType]).
 *     [leftIcon]/[rightIcon] são Composables livres — o chamador decide o que desenhar
 *     (ícone, imagem, badge etc.); [leftIconAction]/[rightIconAction] só tratam o clique.
 *     Nenhum botão é desenhado quando o ícone correspondente é nulo.
 *
 * EN: Design system AppBar with Material 3's four types (see [AppBarType]).
 *     [leftIcon]/[rightIcon] are free Composables — the caller decides what to draw
 *     (icon, image, badge, etc.); [leftIconAction]/[rightIconAction] only handle the
 *     click. No button is drawn when its icon is null.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndroidSduiAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    type: AppBarType = AppBarType.Small,
    leftIcon: (@Composable () -> Unit)? = null,
    leftIconAction: (() -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null,
    rightIconAction: (() -> Unit)? = null,
) {
    val navigationIcon: @Composable () -> Unit = {
        leftIcon?.let { icon ->
            IconButton(onClick = { leftIconAction?.invoke() }) { icon() }
        }
    }
    val actions: @Composable RowScope.() -> Unit = {
        rightIcon?.let { icon ->
            IconButton(onClick = { rightIconAction?.invoke() }) { icon() }
        }
    }

    when (type) {
        AppBarType.Small -> TopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
        )

        AppBarType.CenterAligned -> CenterAlignedTopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
        )

        AppBarType.Medium -> MediumTopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
        )

        AppBarType.Large -> LargeTopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
        )
    }
}
