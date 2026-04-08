package com.douglassantana.domain.usecase

import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.model.NodeDto
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FetchScreenUseCaseTest {

    // region helpers

    private fun useCase(result: Result<NodeDto>) =
        FetchScreenUseCase(FakeSduiRepository(result))

    private val textDto = NodeDto(
        type = "text",
        props = mapOf("text" to JsonPrimitive("Hello SDUI")),
    )

    // endregion

    @Test
    fun `invoke returns success with mapped Node`() = runTest {
        val result = useCase(Result.success(textDto)).invoke("/home")

        assertTrue(result.isSuccess)
        assertEquals("text", result.getOrNull()?.type)
    }

    @Test
    fun `invoke maps props from dto`() = runTest {
        val result = useCase(Result.success(textDto)).invoke("/home")

        assertEquals(JsonPrimitive("Hello SDUI"), result.getOrNull()?.props?.get("text"))
    }

    @Test
    fun `invoke maps children from dto`() = runTest {
        val dto = NodeDto(
            type = "column",
            components = listOf(
                NodeDto(type = "text"),
                NodeDto(type = "button"),
            ),
        )

        val result = useCase(Result.success(dto)).invoke("/home")

        assertEquals(2, result.getOrNull()?.children?.size)
        assertEquals("text",   result.getOrNull()?.children?.get(0)?.type)
        assertEquals("button", result.getOrNull()?.children?.get(1)?.type)
    }

    @Test
    fun `invoke returns failure on repository error`() = runTest {
        val result = useCase(Result.failure(Exception("Network error"))).invoke("/home")

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke passes route to repository`() = runTest {
        val repo = CapturingRepository(Result.success(textDto))
        val result = FetchScreenUseCase(repo).invoke("/detail")

        assertTrue(result.isSuccess)
        assertEquals("/detail", repo.capturedRoute)
    }

    @Test
    fun `invoke propagates null message on anonymous exception`() = runTest {
        val result = useCase(Result.failure(RuntimeException())).invoke("/home")

        assertTrue(result.isFailure)
        assertNull(result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke maps nested children recursively`() = runTest {
        val dto = NodeDto(
            type = "column",
            components = listOf(
                NodeDto(
                    type = "row",
                    components = listOf(NodeDto(type = "text")),
                ),
            ),
        )

        val node = useCase(Result.success(dto)).invoke("/home").getOrThrow()

        val row = node.children.single()
        assertEquals("row", row.type)
        assertEquals("text", row.children.single().type)
    }
}

// region fakes

private class FakeSduiRepository(
    private val result: Result<NodeDto>,
) : SduiRepository {
    override suspend fun fetchScreen(route: String): Result<NodeDto> = result
}

private class CapturingRepository(
    private val result: Result<NodeDto>,
) : SduiRepository {
    var capturedRoute: String? = null
    override suspend fun fetchScreen(route: String): Result<NodeDto> {
        capturedRoute = route
        return result
    }
}

// endregion
