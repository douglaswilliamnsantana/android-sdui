package com.douglassantana.sdui_components.appbar

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.douglassantana.designsystem.components.appbar.AndroidSduiAppBar
import com.douglassantana.sdui_components.extensions.toAppBarType
import com.douglassantana.sdui_components.extensions.toIconVector
import com.douglassantana.sdui_runtime.compose.ComponentRenderer

class SduiAppBarRenderer : ComponentRenderer<SduiAppBar> {

    override val type = SduiAppBar::class

    @Composable
    override fun Render(component: SduiAppBar) {
        AndroidSduiAppBar(
            title = { Text(component.title) },
            type = component.type.toAppBarType(),
            leftIcon = component.leftIcon.toIconVector()?.let { vector ->
                { Icon(imageVector = vector, contentDescription = null) }
            },
            leftIconAction = component.leftIconAction,
            rightIcon = component.rightIcon.toIconVector()?.let { vector ->
                { Icon(imageVector = vector, contentDescription = null) }
            },
            rightIconAction = component.rightIconAction,
        )
    }
}
