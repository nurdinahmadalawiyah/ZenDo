package com.dinzio.zendo.features.timer.domain.usecase

import com.dinzio.zendo.features.timer.data.model.TimerSettings
import com.dinzio.zendo.features.timer.domain.repository.QuickTimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTimerSettingsUseCase @Inject constructor(
    private val repository: QuickTimerRepository
) {
    operator fun invoke(): Flow<TimerSettings> {
        return repository.getTimerSettings()
    }
}