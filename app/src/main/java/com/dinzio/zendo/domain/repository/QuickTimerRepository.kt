package com.dinzio.zendo.domain.repository

import com.dinzio.zendo.domain.model.TimerSettings
import kotlinx.coroutines.flow.Flow

interface QuickTimerRepository {
    fun getTimerSettings(): Flow<TimerSettings>
}