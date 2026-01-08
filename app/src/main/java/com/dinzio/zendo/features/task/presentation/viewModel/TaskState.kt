package com.dinzio.zendo.features.task.presentation.viewModel

import com.dinzio.zendo.features.task.domain.model.TaskModel

data class TaskState(
    val tasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)