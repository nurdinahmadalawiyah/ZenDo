package com.dinzio.zendo.domain.usecase.timer

import com.dinzio.zendo.domain.model.TimerSettings
import com.dinzio.zendo.domain.repository.QuickTimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTimerSettingsUseCase @Inject constructor(
    private val repository: QuickTimerRepository
) {
    operator fun invoke(): Flow<TimerSettings> {
        return repository.getTimerSettings()
    }
}