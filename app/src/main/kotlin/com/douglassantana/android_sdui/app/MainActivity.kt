package com.douglassantana.android_sdui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.douglassantana.designsystem.theme.AndroidSduiTheme
import com.douglassantana.domain.model.ScreenSource
import com.douglassantana.home.HomeScreen
import com.douglassantana.launcher.LauncherScreen
import com.douglassantana.sdui_core.registry.ComponentRegistry
import com.douglassantana.sdui_runtime.renderer.RendererRegistry
import org.koin.android.ext.android.inject

/**
 * Ponto de entrada da aplicação.
 *
 * Responsável por inicializar o tema e alternar entre [LauncherScreen] (escolha da fonte
 * de dados) e [HomeScreen] (renderização da tela SDUI).
 *
 * ---
 *
 * Application entry point.
 *
 * Responsible for initializing the theme and switching between [LauncherScreen] (data
 * source selection) and [HomeScreen] (SDUI screen rendering).
 */
class MainActivity : ComponentActivity() {

    private val componentRegistry: ComponentRegistry by inject()
    private val rendererRegistry: RendererRegistry by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidSduiTheme {
                // Not rememberSaveable on purpose: on process death this resets to
                // LauncherScreen, which is a safe default for a 2-screen sample flow,
                // not a bug to fix.
                var source by remember { mutableStateOf<ScreenSource?>(null) }

                when (val current = source) {
                    null -> LauncherScreen(onSourceSelected = { source = it })
                    else -> HomeScreen(
                        componentRegistry = componentRegistry,
                        rendererRegistry = rendererRegistry,
                        source = current,
                    )
                }
            }
        }
    }
}
