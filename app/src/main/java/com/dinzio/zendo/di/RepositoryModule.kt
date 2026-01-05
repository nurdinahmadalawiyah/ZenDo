package com.dinzio.zendo.di

import com.dinzio.zendo.data.repository.QuickTimerRepositoryImpl
import com.dinzio.zendo.domain.repository.QuickTimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuickTimerRepository(
        impl: QuickTimerRepositoryImpl
    ): QuickTimerRepository
}