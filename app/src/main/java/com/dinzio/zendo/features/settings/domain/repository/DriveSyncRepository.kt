package com.dinzio.zendo.features.settings.domain.repository

import android.content.Intent

interface DriveSyncRepository {
    suspend fun backupToDrive(jsonData: String, userEmail: String): Result<Unit>
    suspend fun restoreFromDrive(userEmail: String): Result<String>
    suspend fun getAuthIntent(userEmail: String): Intent?
}