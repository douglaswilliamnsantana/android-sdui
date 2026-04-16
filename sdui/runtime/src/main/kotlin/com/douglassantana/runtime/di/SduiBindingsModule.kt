package com.douglassantana.runtime.di

import com.douglassantana.core.IProps
import com.douglassantana.core.factory.ComponentFactory
import com.douglassantana.runtime.compose.ComponentRenderer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet

@Module
@InstallIn(SingletonComponent::class)
object SduiBindingsModule {

    /**
     * PT: Fornece um conjunto vazio para inicializar o multibinding Hilt para ComponentFactory.
     * EN: Provides an empty set to initialize the Hilt multibinding for ComponentFactory.
     */
    @Provides
    @ElementsIntoSet
    fun provideEmptyFactorySet(): Set<ComponentFactory<out IProps>> = emptySet()

    /**
     * PT: Fornece um conjunto vazio para inicializar o multibinding Hilt para ComponentRenderer.
     * EN: Provides an empty set to initialize the Hilt multibinding for ComponentRenderer.
     */
    @Provides
    @ElementsIntoSet
    fun provideEmptyRendererSet(): Set<ComponentRenderer<*>> = emptySet()
}