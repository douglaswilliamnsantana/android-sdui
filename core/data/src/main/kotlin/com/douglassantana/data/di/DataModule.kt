package com.douglassantana.data.di

import com.douglassantana.domain.repository.SduiRepository
import com.douglassantana.data.repository.SduiRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt da camada de dados.
 *
 * Vincula as implementações concretas de repositório às interfaces do domínio.
 *
 * ---
 *
 * Hilt module for the data layer.
 *
 * Binds concrete repository implementations to domain interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindSduiRepository(impl: SduiRepositoryImpl): SduiRepository
}
