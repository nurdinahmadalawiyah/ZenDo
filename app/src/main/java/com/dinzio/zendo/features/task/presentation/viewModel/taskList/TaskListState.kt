package com.dinzio.zendo.features.task.presentation.viewModel.taskList

import com.dinzio.zendo.features.task.domain.model.TaskModel

data class TaskListState(
    val tasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)