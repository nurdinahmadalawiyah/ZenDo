package com.dinzio.zendo.features.category.presentation.viewModel.categoryDetail

sealed class CategoryDetailEvent {
    data class LoadCategoryDetail(val id: Int) : CategoryDetailEvent()
    object Refresh : CategoryDetailEvent()
}