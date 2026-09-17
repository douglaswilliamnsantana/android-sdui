package com.douglassantana.data.repository

import com.douglassantana.data.remoteconfig.RemoteConfigClient
import com.douglassantana.domain.error.SduiError
import com.douglassantana.domain.model.Route
import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.model.NodeDto
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * PT: Implementação de [SduiRepository] que busca a tela SDUI via [RemoteConfigClient] em
 * vez de HTTP. A chave de Remote Config é derivada de [Route.path] removendo a barra
 * inicial (ex: `"/home"` -> `"home"`).
 *
 * ---
 *
 * [SduiRepository] implementation that fetches the SDUI screen via [RemoteConfigClient]
 * instead of HTTP. The Remote Config key is derived from [Route.path] by stripping the
 * leading slash (e.g. `"/home"` -> `"home"`).
 */
class RemoteConfigSduiRepositoryImpl(
    private val remoteConfigClient: RemoteConfigClient,
    private val json: Json,
) : SduiRepository {

    override suspend fun fetchScreen(route: Route): Result<NodeDto> =
        try {
            val key = route.path.removePrefix("/")
            val raw = remoteConfigClient.getString(key)
            Result.success(json.decodeFromString(raw))
        } catch (e: SerializationException) {
            Result.failure(SduiError.Serialization(e))
        } catch (e: Exception) {
            Result.failure(SduiError.Unknown(e))
        }
}
