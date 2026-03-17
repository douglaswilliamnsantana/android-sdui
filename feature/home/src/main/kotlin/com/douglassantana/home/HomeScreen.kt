package com.douglassantana.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.registry.ComponentRegistry
import com.douglassantana.sdui_runtime.renderer.RendererRegistry

/**
 * Tela principal da aplicação, responsável por renderizar o layout SDUI
 * recebido do servidor.
 *
 * Observa o [HomeViewModel] e exibe:
 * - Um indicador de carregamento enquanto os dados são buscados.
 * - Uma mensagem de erro caso a requisição falhe.
 * - A árvore de componentes SDUI quando o [com.douglassantana.sdui_core.Node] está disponível.
 *
 * ---
 *
 * Main screen of the application, responsible for rendering the SDUI layout
 * received from the server.
 *
 * Observes [HomeViewModel] and displays:
 * - A loading indicator while data is being fetched.
 * - An error message if the request fails.
 * - The SDUI component tree when the [com.douglassantana.sdui_core.Node] is available.
 */
@Composable
fun HomeScreen(
    componentRegistry: ComponentRegistry,
    rendererRegistry: RendererRegistry,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val node by viewModel.node.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Surface {
        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            error != null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = error ?: "Erro desconhecido")
            }

            node != null -> {
                val component = remember(node) {
                    componentRegistry.create(node!!, SDUIContext())
                }
                rendererRegistry.Render(component)
            }
        }
    }
}
