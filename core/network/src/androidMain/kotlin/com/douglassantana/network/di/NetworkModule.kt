package com.douglassantana.network.di

import com.douglassantana.network.client.buildHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = "http://10.0.2.2:3000/screens"

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = buildHttpClient()
}
