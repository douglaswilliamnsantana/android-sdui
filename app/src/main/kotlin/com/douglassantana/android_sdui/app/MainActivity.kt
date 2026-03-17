package com.douglassantana.android_sdui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.douglassantana.designsystem.theme.AndroidSduiTheme
import com.douglassantana.home.HomeScreen
import com.douglassantana.sdui_core.registry.ComponentRegistry
import com.douglassantana.sdui_runtime.renderer.RendererRegistry
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Ponto de entrada da aplicação.
 *
 * Responsável por inicializar o tema e delegar a renderização
 * da tela ao composable [HomeScreen].
 *
 * ---
 *
 * Application entry point.
 *
 * Responsible for initializing the theme and delegating screen
 * rendering to the [HomeScreen] composable.
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
            AndroidSduiTheme {
                HomeScreen(
                    componentRegistry = componentRegistry,
                    rendererRegistry = rendererRegistry,
                )
            }
        }
    }
}
