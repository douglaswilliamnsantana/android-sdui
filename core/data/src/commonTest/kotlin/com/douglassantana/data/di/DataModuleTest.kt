package com.douglassantana.data.di

import com.douglassantana.data.remoteconfig.FakeRemoteConfigClient
import com.douglassantana.data.remoteconfig.RemoteConfigClient
import com.douglassantana.domain.model.ScreenSource
import com.douglassantana.domain.usecase.FetchScreenUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * PT: `dataModule` é o primeiro lugar do repo a resolver um binding por qualifier escolhido
 * via parâmetro de runtime (`ScreenSource`), e isso não tem nenhum apoio do compilador —
 * `named("backend")`/`named("remoteConfig")` são strings. Este teste é a rede de segurança
 * contra um typo silencioso nesse fiação.
 *
 * ---
 *
 * `dataModule` is the first place in the repo to resolve a binding by a qualifier chosen
 * via a runtime parameter (`ScreenSource`), which has no compiler backing —
 * `named("backend")`/`named("remoteConfig")` are strings. This test is the safety net
 * against a silent typo in that wiring.
 */
class DataModuleTest {

    // dataModule's platformRemoteConfigModule() resolves a real FirebaseRemoteConfig on
    // Android, which needs a live FirebaseApp (unavailable in a plain JVM unit test, no
    // Robolectric here) — override it with the fake, loaded after dataModule so it wins.
    private fun testApp() = koinApplication {
        modules(
            dataModule,
            module {
                single { HttpClient(MockEngine { respondOk() }) }
                single(named("baseUrl")) { "http://localhost/" }
                single<RemoteConfigClient> { FakeRemoteConfigClient() }
            },
        )
    }

    @Test
    fun `resolves FetchScreenUseCase for Backend source`() {
        val koin = testApp().koin

        val useCase: FetchScreenUseCase = koin.get { parametersOf(ScreenSource.Backend) }

        assertNotNull(useCase)
    }

    @Test
    fun `resolves FetchScreenUseCase for RemoteConfig source`() {
        val koin = testApp().koin

        val useCase: FetchScreenUseCase = koin.get { parametersOf(ScreenSource.RemoteConfig) }

        assertNotNull(useCase)
    }
}
