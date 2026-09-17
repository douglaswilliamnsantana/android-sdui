package com.douglassantana.android_sdui.app

import android.app.Application
import com.douglassantana.android_sdui.app.di.appModules
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Classe de Application do projeto.
 *
 * Inicializa o Koin e o grafo de dependências global da aplicação a partir de
 * [appModules]. Deve ser declarada no AndroidManifest.xml como `android:name=".app.App"`.
 *
 * ---
 *
 * Application class for the project.
 *
 * Starts Koin and initializes the application's global dependency graph from
 * [appModules]. Must be declared in AndroidManifest.xml as `android:name=".app.App"`.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Firebase self-initializes via a merged ContentProvider before onCreate() runs;
        // this explicit call is idempotent and just documents the dependency clearly.
        FirebaseApp.initializeApp(this)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModules)
        }
    }
}