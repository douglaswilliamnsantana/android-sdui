package com.douglassantana.data.di

import com.douglassantana.data.remoteconfig.platformRemoteConfigModule
import com.douglassantana.data.repository.RemoteConfigSduiRepositoryImpl
import com.douglassantana.data.repository.SduiRepositoryImpl
import com.douglassantana.domain.model.ScreenSource
import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.domain.usecase.FetchScreenUseCase
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val backendQualifier = named("backend")
private val remoteConfigQualifier = named("remoteConfig")

/**
 * PT: Duas [SduiRepository] qualificadas (backend HTTP e Remote Config) mais um
 * [FetchScreenUseCase] parametrizado por [ScreenSource] que escolhe qual delas injetar.
 * O `when` exaustivo garante que uma futura terceira fonte quebre o build aqui em vez de
 * cair silenciosamente num fallback — mesmo padrão usado em `SduiScreenFactory`.
 *
 * ---
 *
 * Two qualified [SduiRepository] bindings (HTTP backend and Remote Config) plus a
 * [FetchScreenUseCase] parametrized by [ScreenSource] that picks which one to inject.
 * The exhaustive `when` ensures a future third source fails the build here instead of
 * silently falling back — same pattern used in `SduiScreenFactory`.
 */
val dataModule = module {
    single { Json { ignoreUnknownKeys = true; coerceInputValues = true } }

    includes(platformRemoteConfigModule())

    single<SduiRepository>(backendQualifier) {
        SduiRepositoryImpl(
            httpClient = get(),
            baseUrl = get(named("baseUrl")),
        )
    }
    single<SduiRepository>(remoteConfigQualifier) {
        RemoteConfigSduiRepositoryImpl(
            remoteConfigClient = get(),
            json = get(),
        )
    }

    factory { (source: ScreenSource) ->
        val qualifier = when (source) {
            ScreenSource.Backend -> backendQualifier
            ScreenSource.RemoteConfig -> remoteConfigQualifier
        }
        FetchScreenUseCase(repository = get(qualifier))
    }
}
