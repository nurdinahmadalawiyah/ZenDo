package com.dinzio.zendo.features.category.presentation.viewModel.categoryList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.features.category.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryListState())
    val state = _state.asStateFlow()

    init {
        observeCategories()
    }

    private fun observeCategories() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getCategoriesUseCase().collect { categoryList ->
                _state.update { it.copy(categories = categoryList, isLoading = false) }
            }
        }
    }
}