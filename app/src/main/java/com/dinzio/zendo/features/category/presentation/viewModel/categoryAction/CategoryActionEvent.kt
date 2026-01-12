package com.dinzio.zendo.features.category.presentation.viewModel.categoryAction

import com.dinzio.zendo.features.category.domain.model.CategoryModel

sealed class CategoryActionEvent {
    // Input Changes
    data class onNameChange(val name: String) : CategoryActionEvent()
    data class onIconChange(val icon: String) : CategoryActionEvent()

    // Actions
    object onSaveCategory : CategoryActionEvent()
    data class onDeleteCategory(val category: CategoryModel) : CategoryActionEvent()
}