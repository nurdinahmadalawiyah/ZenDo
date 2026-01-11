package com.dinzio.zendo.features.task.presentation.viewModel.taskList

sealed class TaskListEvent {
    object LoadTasks : TaskListEvent()
    data class OnSearchQueryChange(val query: String) : TaskListEvent()
}