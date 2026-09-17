package com.douglassantana.data.remoteconfig

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * PT: No iOS, o app real nunca resolve [RemoteConfigClient] via Koin — o Swift busca o
 * Remote Config direto pelo Firebase iOS SDK e só usa `SduiSdk.parseScreen` pra parsear o
 * JSON (ver KDoc de [RemoteConfigClient]). Esta actual existe só pra manter
 * `commonMain` (o `ScreenSource.RemoteConfig` / `RemoteConfigSduiRepositoryImpl`) compilável
 * e testável a partir do Kotlin nesta plataforma.
 *
 * EN: On iOS, the real app never resolves [RemoteConfigClient] via Koin — Swift fetches
 * Remote Config directly via the Firebase iOS SDK and only uses `SduiSdk.parseScreen` to
 * parse the JSON (see [RemoteConfigClient]'s KDoc). This actual exists only to keep
 * `commonMain` (`ScreenSource.RemoteConfig` / `RemoteConfigSduiRepositoryImpl`) compilable
 * and testable from Kotlin on this platform.
 */
actual fun platformRemoteConfigModule(): Module = module {
    single<RemoteConfigClient> { FakeRemoteConfigClient() }
}
