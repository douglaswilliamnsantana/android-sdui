package com.douglassantana.android_sdui.sduiRuntime.compose

import androidx.compose.runtime.Composable
import com.douglassantana.android_sdui.sduiCore.UIComponent
import kotlin.reflect.KClass

/**
 * Contrato para renderização de um [UIComponent] em Jetpack Compose.
 *
 * Cada implementação é responsável por renderizar um único tipo de componente [T].
 * O [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry] usa [type]
 * para rotear o componente ao renderer correto em tempo de execução.
 *
 * As implementações devem ser registradas no módulo Hilt com `@Binds @IntoSet` para que
 * sejam descobertas automaticamente pelo [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry].
 *
 * @param T O tipo concreto de [UIComponent] que este renderer sabe desenhar.
 * @see com.douglassantana.android_sdui.feature.home.renderer.HomeTextRenderer
 *
 * ---
 *
 * Contract for rendering a [UIComponent] in Jetpack Compose.
 *
 * Each implementation is responsible for rendering a single component type [T].
 * The [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry] uses [type]
 * to route the component to the correct renderer at runtime.
 *
 * Implementations must be registered in the Hilt module with `@Binds @IntoSet` so they are
 * automatically discovered by the [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry].
 *
 * @param T The concrete [UIComponent] type this renderer knows how to draw.
 * @see com.douglassantana.android_sdui.feature.home.renderer.HomeTextRenderer
 */
interface ComponentRenderer<T : UIComponent> {

    /** Referência à classe de [T], usada como chave no mapa do [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry].
     *  Reference to the [T] class, used as a key in the [com.douglassantana.android_sdui.sduiRuntime.renderer.RendererRegistry] map. */
    val type: KClass<T>

    /** Renderiza o [component] como um Composable. / Renders the [component] as a Composable. */
    @Composable
    fun Render(component: T)
}