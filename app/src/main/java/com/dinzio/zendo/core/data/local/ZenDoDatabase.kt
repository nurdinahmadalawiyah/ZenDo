package com.dinzio.zendo.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dinzio.zendo.features.task.data.local.dao.TaskDao
import com.dinzio.zendo.features.task.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZenDoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}