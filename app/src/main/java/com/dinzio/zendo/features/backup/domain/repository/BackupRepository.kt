package com.dinzio.zendo.features.backup.domain.repository

import android.content.Intent
import android.net.Uri
import com.dinzio.zendo.features.backup.domain.model.BackupMetadata
import kotlinx.coroutines.flow.Flow

interface BackupRepository {
    suspend fun exportToFile(uri: Uri): Result<Unit>
    suspend fun importFromFile(uri: Uri): Result<Unit>
    suspend fun backupToDrive(userEmail: String): Result<Unit>
    suspend fun restoreFromDrive(userEmail: String): Result<Unit>
    suspend fun getAuthIntent(userEmail: String): Intent?
    fun observeBackupMetadata(userEmail: String?): Flow<BackupMetadata>
}
