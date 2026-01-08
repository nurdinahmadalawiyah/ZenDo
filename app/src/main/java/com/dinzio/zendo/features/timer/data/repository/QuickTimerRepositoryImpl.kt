package com.dinzio.zendo.features.timer.data.repository

import com.dinzio.zendo.core.util.ZenDoPreferences
import com.dinzio.zendo.features.timer.data.model.TimerSettings
import com.dinzio.zendo.features.timer.domain.repository.QuickTimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuickTimerRepositoryImpl @Inject constructor(
    private val preferences: ZenDoPreferences
) : QuickTimerRepository {

    override fun getTimerSettings(): Flow<TimerSettings> {
        return preferences.timerSettings
    }

}