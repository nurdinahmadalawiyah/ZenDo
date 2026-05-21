package com.dinzio.zendo.features.backup.domain.repository

import android.content.Intent
import android.net.Uri

interface BackupRepository {
    suspend fun exportToFile(uri: Uri): Result<Unit>
    suspend fun importFromFile(uri: Uri): Result<Unit>
    suspend fun backupToDrive(userEmail: String): Result<Unit>
    suspend fun restoreFromDrive(userEmail: String): Result<Unit>
    suspend fun getAuthIntent(userEmail: String): Intent?
}