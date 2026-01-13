package com.dinzio.zendo.features.task.presentation.viewModel.taskAction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.features.category.domain.model.CategoryModel
import com.dinzio.zendo.features.category.domain.usecase.GetCategoriesUseCase
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.domain.usecase.AddTaskUseCase
import com.dinzio.zendo.features.task.domain.usecase.DeleteTaskUseCase
import com.dinzio.zendo.features.task.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskActionViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskActionState())
    val state = _state.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        observeCategories()
    }

    private fun observeCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { list ->
                _categories.value = list
                if (_state.value.categoryIdInput == null && list.isNotEmpty()) {
                    _state.update { it.copy(categoryIdInput = list.first().id) }
                }
            }
        }
    }

    fun onEvent(event: TaskActionEvent) {
        when (event) {
            is TaskActionEvent.OnTitleChange -> _state.update { it.copy(titleInput = event.title) }
            is TaskActionEvent.OnIconChange -> _state.update { it.copy(iconInput = event.icon) }
            is TaskActionEvent.OnCategoryChange -> _state.update { it.copy(categoryIdInput = event.categoryId) }
            is TaskActionEvent.OnSessionCountChange -> _state.update { it.copy(sessionCountInput = event.count) }
            is TaskActionEvent.OnFocusTimeChange -> _state.update { it.copy(focusTimeInput = event.time) }
            is TaskActionEvent.OnBreakTimeChange -> _state.update { it.copy(breakTimeInput = event.time) }

            TaskActionEvent.OnSaveTask -> saveTask()

            is TaskActionEvent.OnDeleteTask -> deleteTask(event.task)
            is TaskActionEvent.OnToggleTask -> viewModelScope.launch {
                updateTaskUseCase(event.task.copy(isCompleted = !event.task.isCompleted))
            }
            else -> Unit
        }
    }

    private fun saveTask() {
        val currentState = _state.value
        if (currentState.titleInput.isBlank()) {
            _state.update { it.copy(errorMessage = "Title cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val newTask = TaskModel(
                    title = currentState.titleInput,
                    icon = currentState.iconInput,
                    categoryId = currentState.categoryIdInput,
                    isCompleted = false,
                    sessionCount = currentState.sessionCountInput,
                    sessionDone = 0,
                    focusTime = currentState.focusTimeInput,
                    breakTime = currentState.breakTimeInput,
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString()
                )
                addTaskUseCase(newTask)
                _state.update { it.copy(isSuccess = true, isSaving = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, errorMessage = e.message) }
            }
        }
    }

    private fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteTaskUseCase(task)
                _state.update { it.copy(isSuccess = true, isDeleting = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, errorMessage = e.message) }
            }
        }
    }
}