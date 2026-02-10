package com.dinzio.zendo.features.task.util

import androidx.navigation.NavController
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.features.task.domain.model.TaskModel

object TaskInteractionHandler {
    /**
     * Menjalankan aksi berdasarkan status task.
     * Jika task selesai, panggil onLocked.
     * Jika belum, panggil onUnlocked.
     */
    fun NavController.navigateToTaskSafely(
        task: TaskModel,
        onLocked: () -> Unit
    ) {
        if (task.isCompleted) {
            onLocked()
        } else {
            task.id?.let { this.navigate(ZenDoRoutes.PomodoroTask.passId(it)) }
        }
    }
}