package com.dinzio.zendo.features.task.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.domain.usecase.AddTaskUseCase
import com.dinzio.zendo.features.task.domain.usecase.DeleteTaskUseCase
import com.dinzio.zendo.features.task.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskActionViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    fun onEvent(event: TaskEvent) {
        viewModelScope.launch {
            when (event) {
                is TaskEvent.OnAddTask -> {
                    val newTask = TaskModel(
                        title = event.title,
                        icon = event.icon,
                        categoryId = event.categoryId,
                        isCompleted = false,
                        sessionCount = 0,
                        sessionDone = 0,
                        createdAt = System.currentTimeMillis().toString(),
                        updatedAt = System.currentTimeMillis().toString()
                    )
                    addTaskUseCase(newTask)
                }
                is TaskEvent.OnDeleteTask -> deleteTaskUseCase(event.task)
                is TaskEvent.OnToggleTask -> {
                    updateTaskUseCase(event.task.copy(isCompleted = !event.task.isCompleted))
                }
                else -> Unit
            }
        }
    }
}