package com.douglassantana.sdui_core.di

import com.douglassantana.sdui_core.log.AndroidSduiLogger
import com.douglassantana.sdui_core.log.SduiLogger
import com.douglassantana.sdui_core.registry.ComponentRegistry
import org.koin.dsl.module

val sduiCoreModule = module {
    single<SduiLogger> { AndroidSduiLogger() }
    single { ComponentRegistry(factories = getAll(), logger = get()) }
}
