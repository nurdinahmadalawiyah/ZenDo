package com.dinzio.zendo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dinzio.zendo.data.local.dao.TaskDao
import com.dinzio.zendo.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZenDoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}