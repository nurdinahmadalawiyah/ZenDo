package com.dinzio.zendo.features.task.domain.repository

import com.dinzio.zendo.features.task.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<TaskModel>>
    suspend fun getTaskById(id: Int): TaskModel?
    suspend fun insertTask(task: TaskModel)
    suspend fun updateTask(task: TaskModel)
    suspend fun deleteTask(task: TaskModel)
}