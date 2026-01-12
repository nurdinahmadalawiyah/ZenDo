package com.dinzio.zendo.features.category.presentation.viewModel.categoryList

sealed class CategoryListEvent {
    object LoadCategories : CategoryListEvent()
    data class OnSearchQueryChange(val query: String) : CategoryListEvent()
}