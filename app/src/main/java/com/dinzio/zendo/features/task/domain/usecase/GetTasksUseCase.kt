package com.dinzio.zendo.features.task.domain.usecase

import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<TaskModel>> = repository.getTasks()
}