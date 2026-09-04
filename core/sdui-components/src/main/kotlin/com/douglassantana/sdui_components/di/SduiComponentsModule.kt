package com.douglassantana.sdui_components.di

import com.douglassantana.sdui_components.appbar.SduiAppBarFactory
import com.douglassantana.sdui_components.appbar.SduiAppBarRenderer
import com.douglassantana.sdui_components.screen.SduiBodyFactory
import com.douglassantana.sdui_components.screen.SduiBottomFactory
import com.douglassantana.sdui_components.screen.SduiHeaderFactory
import com.douglassantana.sdui_components.screen.SduiScreenFactory
import com.douglassantana.sdui_components.screen.SduiScreenRenderer
import com.douglassantana.sdui_components.text.SduiTextFactory
import com.douglassantana.sdui_components.text.SduiTextRenderer
import com.douglassantana.sdui_core.factory.ComponentFactory
import com.douglassantana.sdui_runtime.compose.ComponentRenderer
import org.koin.dsl.bind
import org.koin.dsl.module

val sduiComponentsModule = module {
    single { SduiTextFactory() } bind ComponentFactory::class
    single { SduiTextRenderer() } bind ComponentRenderer::class

    single { SduiHeaderFactory() } bind ComponentFactory::class
    single { SduiBodyFactory() } bind ComponentFactory::class
    single { SduiBottomFactory() } bind ComponentFactory::class

    single { SduiScreenFactory() } bind ComponentFactory::class
    single { SduiScreenRenderer(rendererRegistry = inject()) } bind ComponentRenderer::class

    single { SduiAppBarFactory() } bind ComponentFactory::class
    single { SduiAppBarRenderer() } bind ComponentRenderer::class
}
