package com.dinzio.zendo.core.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.dinzio.zendo.core.util.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusTimerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val FOCUS_TIME_KEY = intPreferencesKey("focus_time")

    val focusTime: Flow<Int> = context.dataStore.data
        .map { it[FOCUS_TIME_KEY] ?: 25 }

    suspend fun setFocusTime(time: Int) {
        context.dataStore.edit { it[FOCUS_TIME_KEY] = time }
    }
}