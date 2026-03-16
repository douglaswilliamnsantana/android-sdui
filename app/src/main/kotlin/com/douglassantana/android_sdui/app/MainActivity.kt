package com.douglassantana.android_sdui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import com.douglassantana.designsystem.theme.AndroidSduiTheme
import com.douglassantana.domain.mapper.NodeMapper
import com.douglassantana.domain.model.NodeDto
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.SduiJson
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

    private val testJson = """
        {
          "type": "text",
          "props": {
            "text": "Hello SDUI",
            "style": {
              "padding": {
                "start": 24,
                "end": 24,
                "top": 32,
                "bottom": 0
              },
              "color": "#1A202C",
              "fontSize": 22,
              "fontWeight": "semi-bold"
            }
          }
        }
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = remember {
                // 1. Desserializa JSON → NodeDto
                val nodeDto = SduiJson.decodeFromString<NodeDto>(testJson)

                // 2. Converte NodeDto → Node
                val node = NodeMapper.toNode(nodeDto)

                // 3. Cria UIComponent via ComponentRegistry
                componentRegistry.create(node, SDUIContext())
            }

            AndroidSduiTheme {
                Surface {
                    rendererRegistry.Render(component)
                }
            }
        }
    }
}