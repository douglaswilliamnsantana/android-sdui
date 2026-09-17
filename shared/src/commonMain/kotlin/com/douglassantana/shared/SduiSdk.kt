package com.douglassantana.shared

import com.douglassantana.domain.mapper.NodeMapper
import com.douglassantana.domain.model.Route
import com.douglassantana.domain.model.ScreenSource
import com.douglassantana.domain.usecase.FetchScreenUseCase
import com.douglassantana.model.NodeDto
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.SduiJson
import kotlinx.serialization.decodeFromString
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform

/**
 * Public entry-point exposed to iOS via the Shared framework.
 *
 * Resolves its dependencies from the Koin graph — the same one the Android app starts
 * from `App.kt`, via [com.douglassantana.shared.di.AppKoin.start]. Swift must call
 * `AppKoin.shared.start()` once, before creating the first `SduiSdk()` (typically in the
 * `@main App`'s `init`); calling `fetchScreen` before that throws immediately.
 *
 * [parseScreen] instead of a `fetchScreenFromRemoteConfig`: the Firebase iOS SDK is a Swift
 * Package, not cleanly cinterop-able from Kotlin/Native without extra tooling, so Swift
 * fetches Remote Config directly via the native SDK and only asks Kotlin to parse the
 * resulting JSON — reusing [NodeMapper]/[SduiJson] instead of duplicating that logic in
 * Swift. The Android app, by contrast, still goes through [FetchScreenUseCase] for both
 * sources (see `core:data`'s `platformRemoteConfigModule`), since the Firebase Android SDK
 * is plain Kotlin with no cinterop concern.
 */
class SduiSdk {

    val context: SDUIContext = SDUIContext()

    /**
     * Fetches and maps an SDUI screen for the given [route] from the HTTP backend.
     * Annotated with [@Throws] so Swift can call it with `try await`.
     *
     * The public boundary stays [String] — the plain Swift-facing type — while [Route]
     * (a Kotlin value class) is used internally, past this entry point.
     */
    @Throws(Exception::class)
    suspend fun fetchScreen(route: String): NodeReader {
        val fetchScreenUseCase: FetchScreenUseCase =
            KoinPlatform.getKoin().get { parametersOf(ScreenSource.Backend) }
        return NodeReader(fetchScreenUseCase(Route(route)).getOrThrow())
    }

    /**
     * Maps a [json] string already fetched by Swift (via the Firebase iOS SDK) into a
     * [NodeReader]. No I/O here — pure parsing, reusing the same [SduiJson]/[NodeMapper]
     * pipeline the backend path uses.
     */
    @Throws(Exception::class)
    fun parseScreen(json: String): NodeReader =
        NodeReader(NodeMapper.toNode(SduiJson.decodeFromString<NodeDto>(json)))
}
