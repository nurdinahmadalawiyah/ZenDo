package com.dinzio.zendo.features.theme.domain.usecase

import com.dinzio.zendo.features.theme.domain.repository.ThemeRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke(mode: String) {
        repository.setThemeMode(mode)
    }
}