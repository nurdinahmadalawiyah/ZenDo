package com.dinzio.zendo.core.di

import com.dinzio.zendo.features.auth.data.repository.AuthRepositoryImpl
import com.dinzio.zendo.features.auth.domain.repository.AuthRepository
import com.dinzio.zendo.features.category.data.repository.CategoryRepositoryImpl
import com.dinzio.zendo.features.category.domain.repository.CategoryRepository
import com.dinzio.zendo.features.settings.data.repository.DriveSyncRepositoryImpl
import com.dinzio.zendo.features.settings.domain.repository.DriveSyncRepository
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
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

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

    @Binds
    @Singleton
    abstract fun bindDriveSyncRepository(
        impl: DriveSyncRepositoryImpl
    ): DriveSyncRepository
}