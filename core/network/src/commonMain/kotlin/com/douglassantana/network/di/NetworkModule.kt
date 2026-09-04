package com.douglassantana.network.di

import com.douglassantana.network.client.buildHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * PT: URL base padrão da API SDUI, específica de cada plataforma — o emulador Android
 *     acessa o host via `10.0.2.2`, enquanto o simulador iOS acessa `localhost` diretamente.
 *
 * EN: Platform-specific default base URL for the SDUI API — the Android emulator
 *     reaches the host via `10.0.2.2`, while the iOS simulator hits `localhost` directly.
 */
expect fun defaultBaseUrl(): String

val networkModule = module {
    single(named("baseUrl")) { defaultBaseUrl() }
    single { buildHttpClient() }
}
