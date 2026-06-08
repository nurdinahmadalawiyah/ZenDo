package com.dinzio.zendo.features.language.data.repository

import com.dinzio.zendo.features.language.data.local.LanguageDataSource
import com.dinzio.zendo.features.language.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val dataSource: LanguageDataSource
) : LanguageRepository {
    override fun getLanguageCode(): Flow<String> {
        return dataSource.languageCode
    }
    override suspend fun setLanguage(code: String) {
        dataSource.setLanguage(code)
    }
}