package com.dinzio.zendo.features.timer_settings.domain.usecase

import com.dinzio.zendo.features.timer_settings.domain.repository.TimerSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFocusTimeUseCase @Inject constructor(
    private val repository: TimerSettingsRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getFocusTime()
    }
}
