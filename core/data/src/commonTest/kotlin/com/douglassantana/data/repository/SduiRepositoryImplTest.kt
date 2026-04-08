package com.douglassantana.data.repository

import com.douglassantana.model.NodeDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SduiRepositoryImplTest {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    private fun buildClient(engine: MockEngine) = HttpClient(engine) {
        install(ContentNegotiation) { json(json) }
    }

    @Test
    fun `fetchScreen returns NodeDto on success`() = runTest {
        val responseBody = """{"type":"screen","props":{},"components":[]}"""
        val engine = MockEngine { _ ->
            respond(
                content = responseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val repo = SduiRepositoryImpl(buildClient(engine), "http://localhost:3000/screens")
        val result = repo.fetchScreen("/home")

        assertTrue(result.isSuccess)
        assertEquals("screen", result.getOrThrow().type)
    }

    @Test
    fun `fetchScreen builds correct URL from baseUrl and route`() = runTest {
        var capturedUrl = ""
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond(
                content = """{"type":"screen","props":{},"components":[]}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val repo = SduiRepositoryImpl(buildClient(engine), "http://localhost:3000/screens")
        repo.fetchScreen("/home")

        assertEquals("http://localhost:3000/screens/home", capturedUrl)
    }

    @Test
    fun `fetchScreen returns failure on HTTP error`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = "Not Found",
                status = HttpStatusCode.NotFound,
            )
        }

        val repo = SduiRepositoryImpl(buildClient(engine), "http://localhost:3000/screens")
        val result = repo.fetchScreen("/missing")

        assertTrue(result.isFailure)
    }

    @Test
    fun `fetchScreen returns NodeDto with components`() = runTest {
        val responseBody = """
            {
              "type": "screen",
              "props": {},
              "components": [
                {"type": "text", "props": {"text": "Hello"}, "components": []}
              ]
            }
        """.trimIndent()

        val engine = MockEngine { _ ->
            respond(
                content = responseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val repo = SduiRepositoryImpl(buildClient(engine), "http://localhost:3000/screens")
        val result = repo.fetchScreen("/home")

        val node = result.getOrThrow()
        assertEquals(1, node.components.size)
        assertEquals("text", node.components.first().type)
    }
}
