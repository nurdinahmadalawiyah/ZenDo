package com.dinzio.zendo.core.di

import com.dinzio.zendo.features.task.data.repository.TaskRepositoryImpl
import com.dinzio.zendo.features.task.domain.repository.TaskRepository
import com.dinzio.zendo.features.timer.data.repository.QuickTimerRepositoryImpl
import com.dinzio.zendo.features.timer.domain.repository.QuickTimerRepository
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
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindQuickTimerRepository(
        impl: QuickTimerRepositoryImpl
    ): QuickTimerRepository
}