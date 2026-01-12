package com.dinzio.zendo.features.category.presentation.viewModel.categoryAction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.features.category.domain.model.CategoryModel
import com.dinzio.zendo.features.category.domain.usecase.AddCategoryUseCase
import com.dinzio.zendo.features.category.domain.usecase.DeleteCategoryUseCase
import com.dinzio.zendo.features.category.domain.usecase.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryActionViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryActionState())
    val state = _state.asStateFlow()

    fun onEvent(event: CategoryActionEvent) {
        when (event) {
            is CategoryActionEvent.onNameChange -> _state.update { it.copy(nameInput = event.name) }
            is CategoryActionEvent.onIconChange -> _state.update { it.copy(iconInput = event.icon) }

            CategoryActionEvent.onSaveCategory -> saveCategory()

            is CategoryActionEvent.onDeleteCategory -> viewModelScope.launch { deleteCategoryUseCase(event.category) }
            else -> Unit
        }
    }

    private fun saveCategory() {
        val currentState = _state.value
        if (currentState.nameInput.isBlank()) {
            _state.update { it.copy(errorMessage = "Name cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val newCategory = CategoryModel(
                    name = currentState.nameInput,
                    icon = currentState.iconInput,
                    taskCount = 0,
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString()
                )
                addCategoryUseCase(newCategory)
                _state.update { it.copy(isSaving = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, errorMessage = e.message) }
            }
        }
    }
}