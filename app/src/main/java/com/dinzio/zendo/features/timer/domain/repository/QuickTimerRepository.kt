package com.dinzio.zendo.features.timer.domain.repository

import com.dinzio.zendo.features.timer.data.model.TimerSettings
import kotlinx.coroutines.flow.Flow

interface QuickTimerRepository {
    fun getTimerSettings(): Flow<TimerSettings>
}