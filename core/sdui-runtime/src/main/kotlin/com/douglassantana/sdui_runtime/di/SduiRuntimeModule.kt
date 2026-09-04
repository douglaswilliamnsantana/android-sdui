package com.douglassantana.sdui_runtime.di

import com.douglassantana.sdui_runtime.renderer.RendererRegistry
import org.koin.dsl.module

val sduiRuntimeModule = module {
    single { RendererRegistry(renderers = getAll()) }
}
