package com.douglassantana.android_sdui.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Classe de Application do projeto.
 *
 * A anotação [@HiltAndroidApp] dispara a geração de código do Hilt e inicializa
 * o grafo de dependências global da aplicação. Deve ser declarada no AndroidManifest.xml
 * como `android:name=".app.App"`.
 *
 * ---
 *
 * Application class for the project.
 *
 * The [@HiltAndroidApp] annotation triggers Hilt's code generation and initializes
 * the global dependency graph. Must be declared in AndroidManifest.xml
 * as `android:name=".app.App"`.
 */
@HiltAndroidApp
class App : Application()