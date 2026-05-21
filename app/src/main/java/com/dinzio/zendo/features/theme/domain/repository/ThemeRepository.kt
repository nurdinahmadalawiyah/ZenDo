package com.dinzio.zendo.features.theme.domain.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getThemeMode(): Flow<String>
    suspend fun setThemeMode(mode: String)
}