package com.dinzio.zendo.features.language.domain.usecase

import com.dinzio.zendo.features.language.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.getLanguageCode()
    }
}