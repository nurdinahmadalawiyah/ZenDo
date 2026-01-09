package com.dinzio.zendo.features.task.presentation.viewModel.taskAction

data class TaskActionState(
    val titleInput: String = "",
    val iconInput: String = "🔭",
    val categoryIdInput: Int? = null,
    val sessionCountInput: Int = 1,

    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)