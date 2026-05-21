package com.dinzio.zendo.features.timer_settings.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.dinzio.zendo.core.util.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimerSettingsDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val FOCUS_TIME_KEY = intPreferencesKey("focus_time")
    private val BREAK_TIME_KEY = intPreferencesKey("break_time")

    val focusTime: Flow<Int> = context.dataStore.data
        .map { it[FOCUS_TIME_KEY] ?: 25 }

    val breakTime: Flow<Int> = context.dataStore.data
        .map { it[BREAK_TIME_KEY] ?: 5 }

    suspend fun setFocusTime(time: Int) {
        context.dataStore.edit { it[FOCUS_TIME_KEY] = time }
    }

    suspend fun setBreakTime(time: Int) {
        context.dataStore.edit { it[BREAK_TIME_KEY] = time }
    }
}
