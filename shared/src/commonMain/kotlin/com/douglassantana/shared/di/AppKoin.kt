package com.douglassantana.shared.di

import com.douglassantana.data.di.dataModule
import com.douglassantana.network.di.networkModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

/**
 * PT: Ponto de entrada exposto ao Swift para iniciar o grafo Koin compartilhado com o
 *     Android — [SduiSdk][com.douglassantana.shared.SduiSdk] resolve suas dependências
 *     a partir dele. `start()` deve ser chamado uma única vez, antes do primeiro
 *     `SduiSdk()`, tipicamente no `init` do `@main App` do SwiftUI. Chamadas repetidas
 *     são no-op — seguro de chamar de novo em previews ou testes.
 *
 * EN: Entry point exposed to Swift to start the Koin graph shared with Android —
 *     [SduiSdk][com.douglassantana.shared.SduiSdk] resolves its dependencies from it.
 *     `start()` must be called once, before the first `SduiSdk()`, typically in the
 *     SwiftUI `@main App`'s `init`. Repeated calls are a no-op — safe to call again
 *     from previews or tests.
 */
object AppKoin {

    /** Inicia o grafo com a baseUrl padrão da plataforma (`localhost` no simulador iOS). */
    fun start() = startIfNeeded()

    /** Inicia o grafo com uma baseUrl customizada — útil para staging ou testes. */
    fun start(baseUrl: String) = startIfNeeded(
        module { single(named("baseUrl")) { baseUrl } }
    )

    private fun startIfNeeded(vararg extraModules: Module) {
        if (KoinPlatform.getKoinOrNull() != null) return
        startKoin {
            allowOverride(true)
            modules(networkModule, dataModule, *extraModules)
        }
    }
}
