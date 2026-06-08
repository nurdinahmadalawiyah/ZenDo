package com.dinzio.zendo.features.theme.domain.usecase

import com.dinzio.zendo.features.theme.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.getThemeMode()
    }
}