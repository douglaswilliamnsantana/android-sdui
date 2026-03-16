package com.douglassantana.sdui_components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.douglassantana.sdui_components.extensions.paddingResolved
import com.douglassantana.sdui_components.extensions.toColor
import com.douglassantana.sdui_components.extensions.toFontStyle
import com.douglassantana.sdui_components.extensions.toFontWeight
import com.douglassantana.sdui_runtime.compose.ComponentRenderer
import javax.inject.Inject

class SduiTextRenderer @Inject constructor() : ComponentRenderer<SduiText> {

    override val type = SduiText::class

    @Composable
    override fun Render(component: SduiText) {
        val style = component.style

        Text(
            text = component.value,
            color = style.color.toColor(),
            fontSize = style.fontSize?.sp ?: 16.sp,
            fontStyle = style.fontStyle.toFontStyle(),
            fontWeight = style.fontWeight.toFontWeight(),
            maxLines = style.maxLines,
            minLines = style.minLines,
            modifier = Modifier
                .paddingResolved(padding = style.padding),
        )
    }
}