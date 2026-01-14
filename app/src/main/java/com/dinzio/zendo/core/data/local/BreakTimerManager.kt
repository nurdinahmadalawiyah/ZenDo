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
class BreakTimerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val BREAK_TIME_KEY = intPreferencesKey("break_time")

    val breakTime: Flow<Int> = context.dataStore.data
        .map { it[BREAK_TIME_KEY] ?: 5 }

    suspend fun setBreakTime(time: Int) {
        context.dataStore.edit { it[BREAK_TIME_KEY] = time }
    }
 }