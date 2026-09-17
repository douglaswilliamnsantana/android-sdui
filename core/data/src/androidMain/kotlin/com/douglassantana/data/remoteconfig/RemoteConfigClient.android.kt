package com.douglassantana.data.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformRemoteConfigModule(): Module = module {
    single { FirebaseRemoteConfig.getInstance() }
    single<RemoteConfigClient> { AndroidFirebaseRemoteConfigClient(get()) }
}
