package com.dinzio.zendo.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dinzio.zendo.features.category.data.local.dao.CategoryDao
import com.dinzio.zendo.features.category.data.local.entity.CategoryEntity
import com.dinzio.zendo.features.task.data.local.dao.TaskDao
import com.dinzio.zendo.features.task.data.local.entity.TaskEntity

@Database(
    entities = [CategoryEntity::class, TaskEntity::class],
    version = 4,
    exportSchema = false
)
abstract class ZenDoDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun taskDao(): TaskDao
}