package com.dinzio.zendo.features.task.data.mapper

import com.dinzio.zendo.features.task.data.local.entity.TaskEntity
import com.dinzio.zendo.features.task.domain.model.TaskModel

fun TaskEntity.toDomain() = TaskModel(
    id = id,
    title = title,
    icon = icon,
    categoryId = categoryId,
    isCompleted = isCompleted,
    sessionCount = sessionCount,
    sessionDone = sessionsDone,
    focusTime = focusTime,
    breakTime = breakTime,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TaskModel.toEntity() = TaskEntity(
    id = id,
    title = title,
    icon = icon,
    categoryId = categoryId,
    isCompleted = isCompleted,
    sessionCount = sessionCount,
    sessionsDone = sessionDone,
    focusTime = focusTime,
    breakTime = breakTime,
    createdAt = createdAt,
    updatedAt = updatedAt
)