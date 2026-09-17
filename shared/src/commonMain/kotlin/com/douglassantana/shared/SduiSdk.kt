package com.douglassantana.shared

import com.douglassantana.domain.model.Route
import com.douglassantana.domain.usecase.FetchScreenUseCase
import com.douglassantana.sdui_core.context.SDUIContext
import org.koin.mp.KoinPlatform

/**
 * Public entry-point exposed to iOS via the Shared framework.
 *
 * Resolves its dependencies from the Koin graph — the same one the Android app starts
 * from `App.kt`, via [com.douglassantana.shared.di.AppKoin.start]. Swift must call
 * `AppKoin.shared.start()` once, before creating the first `SduiSdk()` (typically in the
 * `@main App`'s `init`); calling `fetchScreen` before that throws immediately.
 */
class SduiSdk {

    val context: SDUIContext = SDUIContext()

    private val fetchScreenUseCase: FetchScreenUseCase = KoinPlatform.getKoin().get()

    /**
     * Fetches and maps an SDUI screen for the given [route].
     * Annotated with [@Throws] so Swift can call it with `try await`.
     *
     * The public boundary stays [String] — the plain Swift-facing type — while [Route]
     * (a Kotlin value class) is used internally, past this entry point.
     */
    @Throws(Exception::class)
    suspend fun fetchScreen(route: String): NodeReader =
        NodeReader(fetchScreenUseCase(Route(route)).getOrThrow())
}
