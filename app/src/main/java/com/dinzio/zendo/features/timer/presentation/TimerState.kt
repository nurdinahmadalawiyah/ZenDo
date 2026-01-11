package com.dinzio.zendo.features.timer.presentation

enum class TimerMode {
    FOCUS, SHORT_BREAK
}
data class TimerState(
    val mode: TimerMode = TimerMode.FOCUS,
    val totalTime: Long = 25 * 60 * 1000L,
    val currentTime: Long = 25 * 60 * 1000L,
    val isRunning: Boolean = false,
    val currentSession: Int = 1,
    val totalSessions: Int = 4
)