package com.dinzio.zendo.features.task.domain.model

data class TaskModel(
    val id: Int = 0,
    val title: String,
    val icon: String,
    val categoryId: Int?,
    val isCompleted: Boolean,
    val sessionCount: Int,
    val sessionDone: Int,
    val focusTime: Int,
    val breakTime: Int,
    val createdAt: String,
    val updatedAt: String
)