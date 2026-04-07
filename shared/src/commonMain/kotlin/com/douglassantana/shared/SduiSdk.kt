package com.douglassantana.shared

import com.douglassantana.data.repository.SduiRepositoryImpl
import com.douglassantana.domain.usecase.FetchScreenUseCase
import com.douglassantana.network.client.buildHttpClient
import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.context.SDUIContext

/**
 * Public entry-point exposed to iOS via the Shared framework.
 *
 * @param baseUrl Base URL of the SDUI API (default targets local dev server).
 */
class SduiSdk(baseUrl: String) {

    constructor() : this("http://localhost:3000/screens")

    val context: SDUIContext = SDUIContext()

    private val httpClient = buildHttpClient()
    private val repository = SduiRepositoryImpl(httpClient, baseUrl)
    private val fetchScreenUseCase = FetchScreenUseCase(repository)

    /**
     * Fetches and maps an SDUI screen for the given [route].
     * Annotated with [@Throws] so Swift can call it with `try await`.
     */
    @Throws(Exception::class)
    suspend fun fetchScreen(route: String): NodeReader =
        NodeReader(fetchScreenUseCase(route).getOrThrow())
}
