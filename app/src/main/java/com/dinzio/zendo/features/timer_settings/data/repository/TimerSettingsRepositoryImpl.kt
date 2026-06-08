package com.dinzio.zendo.features.timer_settings.data.repository

import com.dinzio.zendo.features.timer_settings.data.local.TimerSettingsDataSource
import com.dinzio.zendo.features.timer_settings.domain.repository.TimerSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimerSettingsRepositoryImpl @Inject constructor(
    private val dataSource: TimerSettingsDataSource
) : TimerSettingsRepository {

    override fun getFocusTime(): Flow<Int> {
        return dataSource.focusTime
    }

    override suspend fun setFocusTime(time: Int) {
        dataSource.setFocusTime(time)
    }

    override fun getBreakTime(): Flow<Int> {
        return dataSource.breakTime
    }

    override suspend fun setBreakTime(time: Int) {
        dataSource.setBreakTime(time)
    }
}
