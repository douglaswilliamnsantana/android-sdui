package com.douglassantana.network.di

import com.douglassantana.network.client.buildHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named("baseUrl")) { "http://10.0.2.2:3000/screens" }
    single { buildHttpClient() }
}
