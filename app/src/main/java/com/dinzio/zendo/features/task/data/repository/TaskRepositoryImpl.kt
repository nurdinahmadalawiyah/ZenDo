package com.dinzio.zendo.features.task.data.repository

import com.dinzio.zendo.features.task.data.local.dao.TaskDao
import com.dinzio.zendo.features.task.data.mapper.toDomain
import com.dinzio.zendo.features.task.data.mapper.toEntity
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getTasks(): Flow<List<TaskModel>> = taskDao.getAllTasks().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun insertTask(task: TaskModel) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: TaskModel) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: TaskModel) {
        taskDao.deleteTask(task.toEntity())
    }
}