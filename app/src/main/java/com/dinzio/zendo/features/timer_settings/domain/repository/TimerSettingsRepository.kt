package com.dinzio.zendo.features.timer_settings.domain.repository

import kotlinx.coroutines.flow.Flow

interface TimerSettingsRepository {
    fun getFocusTime(): Flow<Int>
    suspend fun setFocusTime(time: Int)
    fun getBreakTime(): Flow<Int>
    suspend fun setBreakTime(time: Int)
}
