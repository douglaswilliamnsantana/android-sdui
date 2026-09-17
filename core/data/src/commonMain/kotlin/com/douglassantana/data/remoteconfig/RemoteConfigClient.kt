package com.douglassantana.data.remoteconfig

import org.koin.core.module.Module

/**
 * PT: Abstração sobre a fonte de Remote Config usada para buscar telas SDUI. Isola
 * [com.douglassantana.data.repository.RemoteConfigSduiRepositoryImpl] de qualquer SDK
 * concreto. No Android, a implementação real usa o Firebase Android SDK
 * ([com.google.firebase.remoteconfig.FirebaseRemoteConfig]) diretamente — ver
 * [platformRemoteConfigModule].
 *
 * No iOS, esse caminho não é usado pelo app de verdade: o Swift busca o Remote Config
 * diretamente via Firebase iOS SDK (SPM, não via Kotlin) e só pede pro `SduiSdk` pra
 * fazer o parsing do JSON — ver `SduiSdk.parseScreen`. A actual `iosMain` deste módulo
 * existe só pra manter esse caminho compilável/testável a partir do Kotlin.
 *
 * ---
 *
 * Abstraction over the Remote Config source used to fetch SDUI screens. Isolates
 * [com.douglassantana.data.repository.RemoteConfigSduiRepositoryImpl] from any concrete SDK.
 * On Android, the real implementation uses the Firebase Android SDK
 * ([com.google.firebase.remoteconfig.FirebaseRemoteConfig]) directly — see
 * [platformRemoteConfigModule].
 *
 * On iOS, this path isn't used by the real app: Swift fetches Remote Config directly via
 * the Firebase iOS SDK (SPM, not through Kotlin) and only asks `SduiSdk` to parse the
 * resulting JSON — see `SduiSdk.parseScreen`. The `iosMain` actual for this module exists
 * only to keep that path compilable/testable from Kotlin.
 */
interface RemoteConfigClient {
    suspend fun getString(key: String): String
}

/**
 * PT: Módulo Koin que registra o [RemoteConfigClient] real de cada plataforma. Incluído
 * por [com.douglassantana.data.di.dataModule].
 *
 * EN: Koin module that registers each platform's real [RemoteConfigClient]. Included by
 * [com.douglassantana.data.di.dataModule].
 */
expect fun platformRemoteConfigModule(): Module
