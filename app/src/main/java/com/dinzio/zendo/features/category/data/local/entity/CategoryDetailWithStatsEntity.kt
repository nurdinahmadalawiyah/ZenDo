package com.dinzio.zendo.features.category.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.dinzio.zendo.features.task.data.local.entity.TaskEntity

data class CategoryDetailWithStatsEntity (
    @Embedded val category: CategoryEntity,
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val tasks: List<TaskEntity>
)