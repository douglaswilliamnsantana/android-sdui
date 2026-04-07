package com.douglassantana.data.repository

import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.model.NodeDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class SduiRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : SduiRepository {

    override suspend fun fetchScreen(route: String): Result<NodeDto> =
        runCatching {
            httpClient.get("$baseUrl$route").body<NodeDto>()
        }
}
