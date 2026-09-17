package com.douglassantana.data.repository

import com.douglassantana.domain.error.SduiError
import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.model.NodeDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import kotlinx.serialization.SerializationException

class SduiRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : SduiRepository {

    override suspend fun fetchScreen(route: String): Result<NodeDto> =
        try {
            Result.success(httpClient.get("$baseUrl$route").body<NodeDto>())
        } catch (e: HttpRequestTimeoutException) {
            Result.failure(SduiError.Timeout(e))
        } catch (e: SerializationException) {
            Result.failure(SduiError.Serialization(e))
        } catch (e: Exception) {
            Result.failure(SduiError.Unknown(e))
        }
}
