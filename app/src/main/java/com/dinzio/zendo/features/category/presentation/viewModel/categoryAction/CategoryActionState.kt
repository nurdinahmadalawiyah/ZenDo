package com.dinzio.zendo.features.category.presentation.viewModel.categoryAction

data class CategoryActionState (
    val id: Int? = null,
    val nameInput: String = "",
    val iconInput: String = "🔭",

    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)