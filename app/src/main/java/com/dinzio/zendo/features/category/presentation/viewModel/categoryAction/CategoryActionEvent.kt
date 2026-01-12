package com.dinzio.zendo.features.category.presentation.viewModel.categoryAction

import com.dinzio.zendo.features.category.domain.model.CategoryModel

sealed class CategoryActionEvent {
    // Input Changes
    data class OnNameChange(val name: String) : CategoryActionEvent()
    data class OnIconChange(val icon: String) : CategoryActionEvent()

    // Actions
    object OnSaveCategory : CategoryActionEvent()
    object OnResetSuccess : CategoryActionEvent()
    data class OnDeleteCategory(val category: CategoryModel) : CategoryActionEvent()
}