package com.dinzio.zendo.features.category.domain.model

import com.dinzio.zendo.features.task.domain.model.TaskModel

data class CategoryDetailModel(
    val category: CategoryModel,
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val tasks: List<TaskModel>
)