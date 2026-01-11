package com.dinzio.zendo.core.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dinzio.zendo.core.util.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val LANGUAGE_KEY = stringPreferencesKey("language_code")

    val languageCode: Flow<String> = context.dataStore.data
        .map { it[LANGUAGE_KEY] ?: "system" }

    suspend fun setLanguage(code: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = code }
    }
}