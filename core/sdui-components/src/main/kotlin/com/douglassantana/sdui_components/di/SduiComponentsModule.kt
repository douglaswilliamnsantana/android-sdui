package com.douglassantana.sdui_components.di

import com.douglassantana.sdui_components.text.SduiTextFactory
import com.douglassantana.sdui_components.text.SduiTextRenderer
import com.douglassantana.sdui_core.factory.ComponentFactory
import com.douglassantana.sdui_runtime.compose.ComponentRenderer
import org.koin.dsl.bind
import org.koin.dsl.module

val sduiComponentsModule = module {
    single { SduiTextFactory() } bind ComponentFactory::class
    single { SduiTextRenderer() } bind ComponentRenderer::class
}
