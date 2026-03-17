package com.douglassantana.network.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Cria e configura uma instância de [HttpClient] para uso no SDUI.
 *
 * Configurações aplicadas:
 * - [ContentNegotiation] com kotlinx.serialization (ignora campos desconhecidos)
 * - [Logging] com nível BODY para debug
 * - [HttpTimeout] com 30s para request e 15s para conexão
 *
 * ---
 *
 * Creates and configures an [HttpClient] instance for use in SDUI.
 *
 * Applied configurations:
 * - [ContentNegotiation] with kotlinx.serialization (ignores unknown fields)
 * - [Logging] with BODY level for debug
 * - [HttpTimeout] with 30s for request and 15s for connection
 */
internal fun buildHttpClient(): HttpClient = HttpClient(OkHttp) {

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.BODY
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
    }
}
