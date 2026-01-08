package com.dinzio.zendo.features.task.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    @ColumnInfo(name = "category_id")
    val categoryId: Int,

    val isCompleted: Boolean = false,
    val sessionsDone: Int = 0,
    val totalSessions: Int = 4,
    val dateCreated: Long = System.currentTimeMillis()
)