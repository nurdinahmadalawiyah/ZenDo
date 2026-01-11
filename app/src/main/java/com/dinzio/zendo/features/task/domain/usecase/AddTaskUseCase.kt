package com.dinzio.zendo.features.task.domain.usecase

import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: TaskModel) {
        if (task.title.isNotBlank()) {
            repository.insertTask(task)
        }
    }
}