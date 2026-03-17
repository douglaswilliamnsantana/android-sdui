package com.douglassantana.data.repository

import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.model.NodeDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Named

/**
 * Implementação de [SduiRepository] usando Ktor como cliente HTTP.
 *
 * Pertence à camada de dados — conecta o contrato do domínio
 * à infraestrutura de rede provida por `core:network`.
 *
 * @param httpClient Cliente HTTP configurado em `core:network`.
 * @param baseUrl URL base da API. Injetada via [@Named("baseUrl")][Named].
 *
 * ---
 *
 * Implementation of [SduiRepository] using Ktor as the HTTP client.
 *
 * Belongs to the data layer — connects the domain contract
 * to the network infrastructure provided by `core:network`.
 *
 * @param httpClient HTTP client configured in `core:network`.
 * @param baseUrl Base URL of the API. Injected via [@Named("baseUrl")][Named].
 */
class SduiRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    @param:Named("baseUrl") private val baseUrl: String,
) : SduiRepository {

    override suspend fun fetchScreen(route: String): Result<NodeDto> =
        runCatching {
            httpClient.get("$baseUrl$route").body<NodeDto>()
        }
}
