package com.dinzio.zendo.features.task.presentation.viewModel

import com.dinzio.zendo.features.task.domain.model.TaskModel

sealed class TaskEvent {
    data class OnAddTask(val title: String, val icon: String, val categoryId: Int) : TaskEvent()
    data class OnDeleteTask(val task: TaskModel) : TaskEvent()
    data class OnToggleTask(val task: TaskModel) : TaskEvent()
    object LoadTasks : TaskEvent()
}
