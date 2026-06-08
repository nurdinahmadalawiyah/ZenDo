package com.dinzio.zendo.features.backup.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dinzio.zendo.core.util.dataStore
import com.dinzio.zendo.features.backup.domain.model.BackupMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BackupMetadataDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val LAST_BACKUP_TIMESTAMP = longPreferencesKey("backup_last_backup_timestamp")
    private val LAST_BACKUP_SIZE_BYTES = longPreferencesKey("backup_last_backup_size_bytes")
    private val LAST_STATUS_MESSAGE = stringPreferencesKey("backup_last_status_message")
    private val LAST_STATUS_SUCCESS = booleanPreferencesKey("backup_last_status_success")
    private val LAST_OPERATION = stringPreferencesKey("backup_last_operation")

    val metadata: Flow<BackupMetadata> = context.dataStore.data.map { preferences ->
        BackupMetadata(
            lastBackupTimestamp = preferences[LAST_BACKUP_TIMESTAMP],
            lastBackupSizeBytes = preferences[LAST_BACKUP_SIZE_BYTES],
            lastStatusMessage = preferences[LAST_STATUS_MESSAGE],
            lastStatusSuccess = preferences[LAST_STATUS_SUCCESS],
            lastOperation = preferences[LAST_OPERATION]
        )
    }

    suspend fun saveSuccessfulBackupMetadata(
        timestamp: Long,
        sizeBytes: Long,
        operation: String,
        statusMessage: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[LAST_BACKUP_TIMESTAMP] = timestamp
            preferences[LAST_BACKUP_SIZE_BYTES] = sizeBytes
            preferences[LAST_STATUS_MESSAGE] = statusMessage
            preferences[LAST_STATUS_SUCCESS] = true
            preferences[LAST_OPERATION] = operation
        }
    }

    suspend fun saveOperationStatus(
        operation: String,
        isSuccess: Boolean,
        statusMessage: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[LAST_STATUS_MESSAGE] = statusMessage
            preferences[LAST_STATUS_SUCCESS] = isSuccess
            preferences[LAST_OPERATION] = operation
        }
    }
}
