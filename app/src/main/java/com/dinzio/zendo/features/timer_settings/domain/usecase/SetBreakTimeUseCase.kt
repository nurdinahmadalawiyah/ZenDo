package com.dinzio.zendo.features.timer_settings.domain.usecase

import com.dinzio.zendo.features.timer_settings.domain.repository.TimerSettingsRepository
import javax.inject.Inject

class SetBreakTimeUseCase @Inject constructor(
    private val repository: TimerSettingsRepository
) {
    suspend operator fun invoke(time: Int) {
        repository.setBreakTime(time)
    }
}
