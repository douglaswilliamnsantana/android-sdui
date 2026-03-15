package com.douglassantana.android_sdui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.douglassantana.designsystem.theme.AndroidSduiTheme
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.registry.ComponentRegistry
import com.douglassantana.sdui_runtime.renderer.RendererRegistry
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Ponto de entrada da aplicação.
 *
 * Responsável por:
 * - Receber via injeção o [ComponentRegistry] e o [RendererRegistry].
 * - Criar o [Node] raiz que representa a tela (em produção, viria de uma API remota).
 * - Converter o [Node] em um [UIComponent] usando o [ComponentRegistry].
 * - Entregar o componente ao [RendererRegistry] para renderização em Compose.
 *
 * O componente é criado dentro de `remember {}` para garantir que a criação aconteça
 * dentro do ciclo de vida do Compose e não seja repetida a cada recomposição.
 *
 * ---
 *
 * Application entry point.
 *
 * Responsible for:
 * - Receiving [ComponentRegistry] and [RendererRegistry] via injection.
 * - Creating the root [Node] that represents the screen (in production, this would come from a remote API).
 * - Converting the [Node] into a [UIComponent] using the [ComponentRegistry].
 * - Handing the component to [RendererRegistry] for Compose rendering.
 *
 * The component is created inside `remember {}` to ensure creation happens
 * within the Compose lifecycle and is not repeated on every recomposition.
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var componentRegistry: ComponentRegistry

    @Inject
    lateinit var rendererRegistry: RendererRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = remember {
                val node = Node(type = "text", props = mapOf("text" to "SDUI com Hilt"))

                componentRegistry.create(
                    node,
                    context = SDUIContext()
                )
            }

            AndroidSduiTheme {
                rendererRegistry.Render(component)
            }
        }
    }
}