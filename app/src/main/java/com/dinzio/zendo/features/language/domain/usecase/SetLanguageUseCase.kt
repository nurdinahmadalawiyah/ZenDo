package com.dinzio.zendo.features.language.domain.usecase

import com.dinzio.zendo.features.language.domain.repository.LanguageRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(code: String) {
        repository.setLanguage(code)
    }
}