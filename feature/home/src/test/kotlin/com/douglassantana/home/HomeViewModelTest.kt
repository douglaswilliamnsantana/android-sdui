package com.douglassantana.home

import app.cash.turbine.test
import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.domain.usecase.FetchScreenUseCase
import com.douglassantana.model.NodeDto
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
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

        assertTrue(vm.isLoading.value)
        assertNull(vm.node.value)
        assertNull(vm.error.value)
    }

    @Test
    fun `loadScreen success - node set and loading stopped`() = runTest {
        val vm = makeViewModel(Result.success(textDto))

        advanceUntilIdle()

        assertFalse(vm.isLoading.value)
        assertEquals("text", vm.node.value?.type)
        assertNull(vm.error.value)
    }

    @Test
    fun `loadScreen success - emits loading then not loading`() = runTest {
        val vm = makeViewModel(Result.success(textDto))

        vm.isLoading.test {
            assertTrue(awaitItem())   // isLoading = true  (initial)
            assertFalse(awaitItem())  // isLoading = false (after fetch)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadScreen failure - error set and loading stopped`() = runTest {
        val vm = makeViewModel(Result.failure(Exception("Timeout")))

        advanceUntilIdle()

        assertFalse(vm.isLoading.value)
        assertEquals("Timeout", vm.error.value)
        assertNull(vm.node.value)
    }

    @Test
    fun `loadScreen failure - emits loading then not loading`() = runTest {
        val vm = makeViewModel(Result.failure(Exception("Error")))

        vm.isLoading.test {
            assertTrue(awaitItem())
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadScreen success - node flow emits non-null value`() = runTest {
        val vm = makeViewModel(Result.success(textDto))

        vm.node.test {
            assertNull(awaitItem())        // initial = null
            assertNotNull(awaitItem())     // after success
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadScreen failure - error flow emits message`() = runTest {
        val vm = makeViewModel(Result.failure(Exception("Server down")))

        vm.error.test {
            assertNull(awaitItem())                   // initial = null
            assertEquals("Server down", awaitItem())  // after failure
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadScreen success - node type matches dto`() = runTest {
        val dto = NodeDto(type = "column")
        val vm = makeViewModel(Result.success(dto))

        advanceUntilIdle()

        assertEquals("column", vm.node.value?.type)
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

        assertEquals(2, vm.node.value?.children?.size)
    }
}
