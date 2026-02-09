package com.dinzio.zendo.core.domain.model

import com.dinzio.zendo.features.category.data.local.entity.CategoryEntity
import com.dinzio.zendo.features.task.data.local.entity.TaskEntity

data class BackupData(
    val tasks: List<TaskEntity>,
    val categories: List<CategoryEntity>,
    val version: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)