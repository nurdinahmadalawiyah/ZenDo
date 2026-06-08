package com.dinzio.zendo.features.language.domain.repository

import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    fun getLanguageCode(): Flow<String>
    suspend fun setLanguage(code: String)
}