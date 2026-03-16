package com.douglassantana.sdui_components.extensions

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.douglassantana.domain.style.IMargin

@Stable
fun Modifier.paddingResolved(
    padding: IMargin? = IMargin(),
    start: Int = 0,
    end: Int = 0,
    top: Int = 0,
    bottom: Int = 0,
) = padding(
    start = (padding?.start ?: start).dp,
    end = (padding?.end ?: end).dp,
    top = (padding?.top ?: top).dp,
    bottom = (padding?.bottom ?: bottom).dp,
)