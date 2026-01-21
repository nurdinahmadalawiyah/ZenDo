package com.dinzio.zendo.features.task.presentation.viewModel.pomodoroTask

import com.dinzio.zendo.core.service.TimerState as ServiceState

enum class TimerMode {
    FOCUS, BREAK
}

data class PomodoroTaskState(
    val taskName: String = "Ready to Focus",
    val selectedMinutes: Int = 25,
    val timerServiceState: ServiceState = ServiceState(),
    val isSettingTimer: Boolean = true,
    val mode: TimerMode = TimerMode.FOCUS,
    val currentSession: Int = 1,
    val totalSession: Int = 4,
    val totalTime: Long = 1500L,
    val currentTime: Long = 1500L,
    val isRunning: Boolean = false,
)