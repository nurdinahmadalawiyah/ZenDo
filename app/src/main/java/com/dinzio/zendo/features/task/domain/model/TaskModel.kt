package com.dinzio.zendo.features.task.domain.model

data class TaskModel(
    val id: Int = 0,
    val title: String,
    val icon: String,
    val categoryId: Int?,
    val isCompleted: Boolean,
    val sessionDone: Int,
    val sessionCount: Int,
    val focusTime: Int,
    val breakTime: Int,
    val lastSecondsLeft: Long,
    val lastMode: String,
    val isInIntermediaryState: Boolean,
    val createdAt: String,
    val updatedAt: String
)