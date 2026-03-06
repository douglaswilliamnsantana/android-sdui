package com.douglassantana.android_sdui.sduiRuntime.renderer

import android.util.Log
import androidx.compose.runtime.Composable
import com.douglassantana.android_sdui.sduiCore.UIComponent
import com.douglassantana.android_sdui.sduiRuntime.compose.ComponentRenderer
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Registro central de renderers de componentes SDUI.
 *
 * Recebe via injeção (Hilt multibindings) o conjunto de todos os [ComponentRenderer]
 * registrados no grafo e os organiza em um mapa indexado por [ComponentRenderer.type].
 *
 * Ao receber um [UIComponent], resolve o renderer correspondente pelo tipo concreto do
 * componente via [renderTyped], que captura o tipo genérico [T] de forma segura, evitando
 * casts inseguros por type erasure.
 *
 * Após renderizar o componente pai, renderiza seus filhos recursivamente, garantindo que
 * toda a árvore de [UIComponent] seja desenhada na ordem correta.
 *
 * Se nenhum renderer for encontrado para o tipo do componente, emite um aviso via [Log.w]
 * e retorna sem renderizar nada — sem crashar a aplicação.
 *
 * ---
 *
 * Central registry for SDUI component renderers.
 *
 * Receives via injection (Hilt multibindings) the full set of registered [ComponentRenderer]
 * instances and organizes them in a map indexed by [ComponentRenderer.type].
 *
 * When given a [UIComponent], it resolves the matching renderer by the component's concrete type
 * via [renderTyped], which captures the generic type [T] safely, avoiding unsafe casts from type erasure.
 *
 * After rendering the parent component, it renders its children recursively, ensuring the
 * entire [UIComponent] tree is drawn in the correct order.
 *
 * If no renderer is found for the component type, a warning is emitted via [Log.w]
 * and returns without rendering anything — without crashing the app.
 */
class RendererRegistry @Inject constructor(
    renderers: Set<@JvmSuppressWildcards ComponentRenderer<*>>
) {

    private val rendererMap =
        renderers.associateBy { it.type }

    @Composable
    fun Render(component: UIComponent) {
        renderTyped(component, component::class)
    }

    /**
     * Captura o tipo concreto [T] do componente para garantir type-safety no cast.
     * O cast `component as T` é seguro pois [type] foi obtido de `component::class`,
     * ou seja, o objeto é garantidamente do tipo [T].
     *
     * Captures the concrete type [T] of the component to ensure type-safety in the cast.
     * The `component as T` cast is safe because [type] was obtained from `component::class`,
     * meaning the object is guaranteed to be of type [T].
     */
    @Suppress("UNCHECKED_CAST")
    @Composable
    private fun <T : UIComponent> renderTyped(
        component: UIComponent,
        type: KClass<T>
    ) {
        val renderer = rendererMap[type] as? ComponentRenderer<T>
        if (renderer == null) {
            Log.w("RendererRegistry", "No renderer registered for type '${type.simpleName}'. Nothing will be rendered.")
            return
        }
        renderer.Render(component as T)
        component.children.forEach { child -> Render(child) }
    }
}