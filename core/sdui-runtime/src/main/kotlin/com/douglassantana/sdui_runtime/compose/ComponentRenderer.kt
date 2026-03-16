package com.douglassantana.sdui_runtime.compose

import androidx.compose.runtime.Composable
import com.douglassantana.sdui_core.UIComponent
import kotlin.reflect.KClass

/**
 * Contrato para renderização de um [UIComponent] em Jetpack Compose.
 *
 * Cada implementação é responsável por renderizar um único tipo de componente [T].
 * O [RendererRegistry] usa [type]
 * para rotear o componente ao renderer correto em tempo de execução.
 *
 * As implementações devem ser registradas no módulo Hilt com `@Binds @IntoSet` para que
 * sejam descobertas automaticamente pelo [RendererRegistry].
 *
 * @param T O tipo concreto de [UIComponent] que este renderer sabe desenhar.
 *
 * ---
 *
 * Contract for rendering a [UIComponent] in Jetpack Compose.
 *
 * Each implementation is responsible for rendering a single component type [T].
 * The [RendererRegistry] uses [type]
 * to route the component to the correct renderer at runtime.
 *
 * Implementations must be registered in the Hilt module with `@Binds @IntoSet` so they are
 * automatically discovered by the [RendererRegistry].
 *
 * @param T The concrete [UIComponent] type this renderer knows how to draw.
 */
interface ComponentRenderer<T : UIComponent> {

    /** Referência à classe de [T], usada como chave no mapa do [RendererRegistry].
     *  Reference to the [T] class, used as a key in the [RendererRegistry] map. */
    val type: KClass<T>

    /** Renderiza o [component] como um Composable. / Renders the [component] as a Composable. */
    @Composable
    fun Render(component: T)
}