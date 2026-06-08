package com.dinzio.zendo.features.theme.data.repository

import com.dinzio.zendo.features.theme.data.local.ThemeDataSource
import com.dinzio.zendo.features.theme.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val dataSource: ThemeDataSource
) : ThemeRepository {
    override fun getThemeMode(): Flow<String> {
        return dataSource.themeMode
    }

    override suspend fun setThemeMode(mode: String) {
        dataSource.setThemeMode(mode)
    }
}