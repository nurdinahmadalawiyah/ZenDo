package com.dinzio.zendo.features.task.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    val title: String,
    val icon: String,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    val isCompleted: Boolean = false,
    val sessionsDone: Int,
    val sessionCount: Int,
    val createdAt: String,
    val updatedAt: String
)