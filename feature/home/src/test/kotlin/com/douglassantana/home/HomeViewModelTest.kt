package com.douglassantana.home

import app.cash.turbine.test
import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.domain.usecase.FetchScreenUseCase
import com.douglassantana.model.NodeDto
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.state.ScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.JsonPrimitive
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region helpers

    private fun makeViewModel(result: Result<NodeDto>): HomeViewModel {
        val repo = object : SduiRepository {
            override suspend fun fetchScreen(route: String) = result
        }
        return HomeViewModel(FetchScreenUseCase(repo))
    }

    private val textDto = NodeDto(
        type = "text",
        props = mapOf("text" to JsonPrimitive("Hello SDUI")),
    )

    // endregion

    @Test
    fun `initial state is loading`() = runTest {
        val repo = object : SduiRepository {
            override suspend fun fetchScreen(route: String): Result<NodeDto> {
                // never completes during this test
                kotlinx.coroutines.awaitCancellation()
            }
        }
        val vm = HomeViewModel(FetchScreenUseCase(repo))

        assertIs<ScreenUiState.Loading>(vm.uiState.value)
    }

    @Test
    fun `loadScreen success - state is success with node`() = runTest {
        val vm = makeViewModel(Result.success(textDto))

        advanceUntilIdle()

        val state = assertIs<ScreenUiState.Success<Node>>(vm.uiState.value)
        assertEquals("text", state.data.type)
    }

    @Test
    fun `loadScreen success - emits loading then success`() = runTest {
        val vm = makeViewModel(Result.success(textDto))

        vm.uiState.test {
            assertIs<ScreenUiState.Loading>(awaitItem())
            assertIs<ScreenUiState.Success<Node>>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadScreen failure - state is error with message`() = runTest {
        val vm = makeViewModel(Result.failure(Exception("Timeout")))

        advanceUntilIdle()

        val state = assertIs<ScreenUiState.Error>(vm.uiState.value)
        assertEquals("Timeout", state.message)
    }

    @Test
    fun `loadScreen failure - emits loading then error`() = runTest {
        val vm = makeViewModel(Result.failure(Exception("Error")))

        vm.uiState.test {
            assertIs<ScreenUiState.Loading>(awaitItem())
            assertIs<ScreenUiState.Error>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadScreen success - node type matches dto`() = runTest {
        val dto = NodeDto(type = "column")
        val vm = makeViewModel(Result.success(dto))

        advanceUntilIdle()

        val state = assertIs<ScreenUiState.Success<Node>>(vm.uiState.value)
        assertEquals("column", state.data.type)
    }

    @Test
    fun `loadScreen success - node children are mapped`() = runTest {
        val dto = NodeDto(
            type = "column",
            components = listOf(
                NodeDto(type = "text"),
                NodeDto(type = "button"),
            ),
        )
        val vm = makeViewModel(Result.success(dto))

        advanceUntilIdle()

        val state = assertIs<ScreenUiState.Success<Node>>(vm.uiState.value)
        assertEquals(2, state.data.children.size)
    }
}
