package com.dinzio.zendo.core.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.dinzio.zendo.domain.model.TimerSettings

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "zendo_settings")

class ZenDoPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val FOCUS_KEY = longPreferencesKey("focus_duration")
    private val BREAK_KEY = longPreferencesKey("break_duration")
    private val SESSION_KEY = intPreferencesKey("total_sessions")

    val timerSettings: Flow<TimerSettings> = context.dataStore.data.map { preferences ->
        TimerSettings(
            focusDuration = preferences[FOCUS_KEY] ?: (25 * 60 * 1000L), // Default 25
            breakDuration = preferences[BREAK_KEY] ?: (5 * 60 * 1000L),  // Default 5
            totalSessions = preferences[SESSION_KEY] ?: 4
        )
    }

    suspend fun saveTimerSettings(settings: TimerSettings) {
        context.dataStore.edit { preferences ->
            preferences[FOCUS_KEY] = settings.focusDuration
            preferences[BREAK_KEY] = settings.breakDuration
            preferences[SESSION_KEY] = settings.totalSessions
        }
    }
}