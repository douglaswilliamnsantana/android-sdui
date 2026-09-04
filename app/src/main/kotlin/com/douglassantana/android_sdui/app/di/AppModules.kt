package com.douglassantana.android_sdui.app.di

import com.douglassantana.data.di.dataModule
import com.douglassantana.home.di.homeModule
import com.douglassantana.network.di.networkModule
import com.douglassantana.sdui_components.di.sduiComponentsModule
import com.douglassantana.sdui_core.di.sduiCoreModule
import com.douglassantana.sdui_runtime.di.sduiRuntimeModule

/**
 * PT: Agrega todos os módulos Koin da aplicação, um por módulo Gradle,
 *     para serem carregados juntos em [com.douglassantana.android_sdui.app.App].
 *
 * EN: Aggregates all Koin modules across Gradle modules, to be loaded
 *     together in [com.douglassantana.android_sdui.app.App].
 */
val appModules = listOf(
    networkModule,
    dataModule,
    sduiCoreModule,
    sduiRuntimeModule,
    sduiComponentsModule,
    homeModule,
)
