package com.dinzio.zendo.features.category.presentation.viewModel.categoryDetail

import com.dinzio.zendo.features.category.domain.model.CategoryDetailModel
import com.dinzio.zendo.features.task.domain.model.TaskModel

data class CategoryDetailState(
    val categoryDetail: CategoryDetailModel? = null,
    val completedTasks: List<TaskModel> = emptyList(),
    val pendingTasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)