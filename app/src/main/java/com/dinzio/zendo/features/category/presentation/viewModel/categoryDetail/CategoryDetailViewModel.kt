package com.dinzio.zendo.features.category.presentation.viewModel.categoryDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.features.category.domain.usecase.GetCategoryDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryDetailState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>("categoryId")?.let { id ->
            onEvent(CategoryDetailEvent.LoadCategoryDetail(id))
        }
    }

    fun onEvent(event: CategoryDetailEvent) {
        when (event) {
            is CategoryDetailEvent.LoadCategoryDetail -> {
                getDetail(event.id)
            }
            CategoryDetailEvent.Refresh -> {
                state.value.categoryDetail?.category?.id?.let { getDetail(it) }
            }
        }
    }

    private fun getDetail(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getCategoryDetailUseCase(id)
                .catch { e ->
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An unexpected error occurred"
                    )}
                }
                .collect { detail ->
                    val allTasks = detail?.tasks ?: emptyList()
                    val pending = allTasks.filter { !it.isCompleted }
                    val completed = allTasks.filter { it.isCompleted }

                    _state.update { it.copy(
                        isLoading = false,
                        categoryDetail = detail,
                        completedTasks = completed,
                        pendingTasks = pending,
                        errorMessage = if (detail == null) "Category not found" else null
                    )}
                }
        }
    }
}