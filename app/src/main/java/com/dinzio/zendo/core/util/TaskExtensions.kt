package com.dinzio.zendo.core.util

import android.content.Context
import com.dinzio.zendo.core.service.TimerService
import com.dinzio.zendo.features.task.domain.model.TaskModel

fun TaskModel.startTimer(context: Context) {
    val defaultDuration = if (this.lastMode == "BREAK") {
        this.breakTime * 60L
    } else {
        this.focusTime * 60L
    }

    val durationToUse = if (this.lastSecondsLeft > 0L) {
        this.lastSecondsLeft
    } else {
        defaultDuration
    }

    TimerService.sendAction(
        context = context,
        action = TimerService.ACTION_START,
        taskName = this.title,
        taskId = this.id,
        duration = durationToUse
    )
}