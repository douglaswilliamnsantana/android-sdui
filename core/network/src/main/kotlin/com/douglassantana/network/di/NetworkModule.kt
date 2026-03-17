package com.douglassantana.network.di

import com.douglassantana.network.client.buildHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Named
import javax.inject.Singleton

/**
 * Módulo Hilt da camada de rede.
 *
 * Fornece exclusivamente dependências de infraestrutura HTTP:
 * - [HttpClient] — cliente Ktor configurado como singleton
 * - [String] com qualificador `"baseUrl"` — URL base da API
 *
 * O vínculo entre [com.douglassantana.data.repository.SduiRepository] e sua
 * implementação é responsabilidade do `DataModule` em `core:data`.
 *
 * ---
 *
 * Hilt module for the network layer.
 *
 * Provides only HTTP infrastructure dependencies:
 * - [HttpClient] — configured Ktor client as singleton
 * - [String] with `"baseUrl"` qualifier — API base URL
 *
 * Binding [com.douglassantana.data.repository.SduiRepository] to its
 * implementation is the responsibility of `DataModule` in `core:data`.
 */
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
