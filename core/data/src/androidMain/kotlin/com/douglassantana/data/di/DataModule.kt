package com.douglassantana.data.di

import com.douglassantana.data.repository.SduiRepositoryImpl
import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.domain.usecase.FetchScreenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSduiRepository(
        httpClient: HttpClient,
        @Named("baseUrl") baseUrl: String,
    ): SduiRepository = SduiRepositoryImpl(httpClient, baseUrl)

    @Provides
    @Singleton
    fun provideFetchScreenUseCase(repository: SduiRepository): FetchScreenUseCase =
        FetchScreenUseCase(repository)
}
