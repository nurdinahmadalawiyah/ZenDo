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
    fun observeMetadata(accountEmail: String?): Flow<BackupMetadata> = context.dataStore.data.map { preferences ->
        val keyPrefix = accountKeyPrefix(accountEmail)
        BackupMetadata(
            lastBackupTimestamp = preferences[longPreferencesKey("${keyPrefix}_last_backup_timestamp")],
            lastBackupSizeBytes = preferences[longPreferencesKey("${keyPrefix}_last_backup_size_bytes")],
            lastStatusMessage = preferences[stringPreferencesKey("${keyPrefix}_last_status_message")],
            lastStatusSuccess = preferences[booleanPreferencesKey("${keyPrefix}_last_status_success")],
            lastOperation = preferences[stringPreferencesKey("${keyPrefix}_last_operation")]
        )
    }

    suspend fun saveSuccessfulBackupMetadata(
        accountEmail: String?,
        timestamp: Long,
        sizeBytes: Long,
        operation: String,
        statusMessage: String
    ) {
        val keyPrefix = accountKeyPrefix(accountEmail)
        context.dataStore.edit { preferences ->
            preferences[longPreferencesKey("${keyPrefix}_last_backup_timestamp")] = timestamp
            preferences[longPreferencesKey("${keyPrefix}_last_backup_size_bytes")] = sizeBytes
            preferences[stringPreferencesKey("${keyPrefix}_last_status_message")] = statusMessage
            preferences[booleanPreferencesKey("${keyPrefix}_last_status_success")] = true
            preferences[stringPreferencesKey("${keyPrefix}_last_operation")] = operation
        }
    }

    suspend fun saveOperationStatus(
        accountEmail: String?,
        operation: String,
        isSuccess: Boolean,
        statusMessage: String
    ) {
        val keyPrefix = accountKeyPrefix(accountEmail)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("${keyPrefix}_last_status_message")] = statusMessage
            preferences[booleanPreferencesKey("${keyPrefix}_last_status_success")] = isSuccess
            preferences[stringPreferencesKey("${keyPrefix}_last_operation")] = operation
        }
    }

    private fun accountKeyPrefix(accountEmail: String?): String {
        val normalizedEmail = accountEmail
            ?.trim()
            ?.lowercase()
            ?.takeIf { it.isNotEmpty() }
            ?: "guest"

        return buildString("backup_metadata_".length + normalizedEmail.length) {
            append("backup_metadata_")
            normalizedEmail.forEach { character ->
                append(
                    if (character.isLetterOrDigit()) {
                        character
                    } else {
                        '_'
                    }
                )
            }
        }
    }
}
