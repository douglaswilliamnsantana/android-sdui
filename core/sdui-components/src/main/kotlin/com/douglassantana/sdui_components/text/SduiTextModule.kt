package com.douglassantana.sdui_components.text

import com.douglassantana.sdui_core.IProps
import com.douglassantana.sdui_core.factory.ComponentFactory
import com.douglassantana.sdui_runtime.compose.ComponentRenderer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class SduiTextModule {

    @Binds
    @IntoSet
    abstract fun bindSduiTextFactory(
        factory: SduiTextFactory
    ): ComponentFactory<out IProps>

    @Binds
    @IntoSet
    abstract fun bindSduiTextRenderer(
        renderer: SduiTextRenderer
    ): ComponentRenderer<*>
}
