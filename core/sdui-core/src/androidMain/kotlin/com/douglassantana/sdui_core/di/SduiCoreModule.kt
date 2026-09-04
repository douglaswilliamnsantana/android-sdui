package com.douglassantana.sdui_core.di

import com.douglassantana.sdui_core.registry.ComponentRegistry
import org.koin.dsl.module

val sduiCoreModule = module {
    single { ComponentRegistry(factories = getAll()) }
}
