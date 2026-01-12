package com.dinzio.zendo.core.di

import android.content.Context
import androidx.room.Room
import com.dinzio.zendo.core.data.local.ZenDoDatabase
import com.dinzio.zendo.features.category.data.local.dao.CategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.dinzio.zendo.features.task.data.local.dao.TaskDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ZenDoDatabase {
        return Room.databaseBuilder(
            context,
            ZenDoDatabase::class.java,
            "zendo_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: ZenDoDatabase): CategoryDao {
        return db.categoryDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: ZenDoDatabase): TaskDao {
        return db.taskDao()
    }
}