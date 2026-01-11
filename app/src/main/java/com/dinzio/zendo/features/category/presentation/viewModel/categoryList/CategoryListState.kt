package com.dinzio.zendo.features.category.presentation.viewModel.categoryList

import com.dinzio.zendo.features.category.domain.model.CategoryModel

data class CategoryListState(
    val categories: List<CategoryModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)