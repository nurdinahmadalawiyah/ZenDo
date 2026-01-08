package com.dinzio.zendo.features.task.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.features.task.domain.usecase.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskState())
    val state = _state.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getTasksUseCase().collect { taskList ->
                _state.update { it.copy(tasks = taskList, isLoading = false) }
            }
        }
    }
}