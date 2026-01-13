package com.dinzio.zendo.features.task.presentation.viewModel.taskAction

import com.dinzio.zendo.features.task.domain.model.TaskModel

sealed class TaskActionEvent {
    // Input Changes
    data class OnTitleChange(val title: String) : TaskActionEvent()
    data class OnIconChange(val icon: String) : TaskActionEvent()
    data class OnCategoryChange(val categoryId: Int?) : TaskActionEvent()
    data class OnSessionCountChange(val count: Int) : TaskActionEvent()
    data class OnFocusTimeChange(val time: Int) : TaskActionEvent()
    data class OnBreakTimeChange(val time: Int) : TaskActionEvent()

    // Actions
    object OnSaveTask : TaskActionEvent()
    data class OnDeleteTask(val task: TaskModel) : TaskActionEvent()
    data class OnToggleTask(val task: TaskModel) : TaskActionEvent()
}