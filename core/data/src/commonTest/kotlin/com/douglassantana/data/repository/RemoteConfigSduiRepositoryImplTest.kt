package com.douglassantana.data.repository

import com.douglassantana.data.remoteconfig.RemoteConfigClient
import com.douglassantana.domain.error.SduiError
import com.douglassantana.domain.model.Route
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RemoteConfigSduiRepositoryImplTest {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    private fun fakeClient(values: Map<String, String>) = object : RemoteConfigClient {
        var requestedKey: String? = null
        override suspend fun getString(key: String): String {
            requestedKey = key
            return values[key] ?: throw NoSuchElementException("No Remote Config value for key '$key'")
        }
    }

    @Test
    fun `fetchScreen derives the key from route path`() = runTest {
        val client = fakeClient(mapOf("home" to """{"type":"screen","props":{},"components":[]}"""))
        val repo = RemoteConfigSduiRepositoryImpl(client, json)

        repo.fetchScreen(Route("/home"))

        assertEquals("home", client.requestedKey)
    }

    @Test
    fun `fetchScreen returns NodeDto on success`() = runTest {
        val client = fakeClient(mapOf("home" to """{"type":"screen","props":{},"components":[]}"""))
        val repo = RemoteConfigSduiRepositoryImpl(client, json)

        val result = repo.fetchScreen(Route("/home"))

        assertTrue(result.isSuccess)
        assertEquals("screen", result.getOrThrow().type)
    }

    @Test
    fun `fetchScreen returns Serialization error on malformed JSON`() = runTest {
        val client = fakeClient(mapOf("home" to "not json"))
        val repo = RemoteConfigSduiRepositoryImpl(client, json)

        val result = repo.fetchScreen(Route("/home"))

        assertTrue(result.isFailure)
        assertIs<SduiError.Serialization>(result.exceptionOrNull())
    }

    @Test
    fun `fetchScreen returns Unknown error when key is missing`() = runTest {
        val client = fakeClient(emptyMap())
        val repo = RemoteConfigSduiRepositoryImpl(client, json)

        val result = repo.fetchScreen(Route("/missing"))

        assertTrue(result.isFailure)
        assertIs<SduiError.Unknown>(result.exceptionOrNull())
    }
}
