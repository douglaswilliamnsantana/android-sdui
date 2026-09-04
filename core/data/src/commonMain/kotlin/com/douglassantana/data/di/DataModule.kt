package com.douglassantana.data.di

import com.douglassantana.data.repository.SduiRepositoryImpl
import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.domain.usecase.FetchScreenUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single<SduiRepository> {
        SduiRepositoryImpl(
            httpClient = get(),
            baseUrl = get(named("baseUrl")),
        )
    }
    single { FetchScreenUseCase(repository = get()) }
}
