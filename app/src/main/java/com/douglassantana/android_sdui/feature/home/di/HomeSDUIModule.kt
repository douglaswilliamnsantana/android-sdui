package com.douglassantana.android_sdui.feature.home.di

import com.douglassantana.android_sdui.feature.home.factory.HomeTextFactory
import com.douglassantana.android_sdui.feature.home.renderer.HomeTextRenderer
import com.douglassantana.android_sdui.sduiCore.factory.ComponentFactory
import com.douglassantana.android_sdui.sduiRuntime.compose.ComponentRenderer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

/**
 * Módulo Hilt responsável por registrar os componentes SDUI da feature Home.
 *
 * Usa `@Binds @IntoSet` para adicionar [HomeTextFactory] e [HomeTextRenderer] aos sets
 * globais de [ComponentFactory] e [ComponentRenderer], que são injetados respectivamente
 * no [com.douglassantana.android_sdui.sduiCore.registry.ComponentRegistry] e no
 * [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry].
 *
 * Para adicionar um novo componente à feature home, basta criar a factory e o renderer
 * e registrá-los aqui com os mesmos bindings.
 *
 * ---
 *
 * Hilt module responsible for registering the Home feature's SDUI components.
 *
 * Uses `@Binds @IntoSet` to add [HomeTextFactory] and [HomeTextRenderer] to the global
 * sets of [ComponentFactory] and [ComponentRenderer], which are injected respectively into
 * [com.douglassantana.android_sdui.sduiCore.registry.ComponentRegistry] and
 * [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry].
 *
 * To add a new component to the home feature, simply create the factory and renderer
 * and register them here with the same bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class HomeSDUIModule {

    @Binds
    @IntoSet
    abstract fun bindHomeTextFactory(
        factory: HomeTextFactory
    ): ComponentFactory

    @Binds
    @IntoSet
    abstract fun bindHomeTextRenderer(
        renderer: HomeTextRenderer
    ): ComponentRenderer<*>
}