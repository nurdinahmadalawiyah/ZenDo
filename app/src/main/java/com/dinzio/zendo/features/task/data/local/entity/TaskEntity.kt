package com.dinzio.zendo.features.task.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import com.dinzio.zendo.features.category.data.local.entity.CategoryEntity

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["category_id"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val icon: String,
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    val isCompleted: Boolean = false,
    val sessionsDone: Int,
    val sessionCount: Int,
    val createdAt: String,
    val updatedAt: String
)