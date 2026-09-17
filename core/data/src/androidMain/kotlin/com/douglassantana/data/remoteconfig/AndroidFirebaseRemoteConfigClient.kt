package com.douglassantana.data.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await

/**
 * PT: Implementação Android de [RemoteConfigClient], baseada no Firebase Android SDK.
 * `fetchAndActivate()` busca e ativa os valores mais recentes antes de ler [key].
 *
 * EN: Android implementation of [RemoteConfigClient], backed by the Firebase Android SDK.
 * `fetchAndActivate()` fetches and activates the latest values before reading [key].
 */
class AndroidFirebaseRemoteConfigClient(
    private val remoteConfig: FirebaseRemoteConfig,
) : RemoteConfigClient {

    override suspend fun getString(key: String): String {
        remoteConfig.fetchAndActivate().await()
        val value = remoteConfig.getString(key)
        if (value.isBlank()) {
            throw NoSuchElementException("No Remote Config value for key '$key'")
        }
        return value
    }
}
