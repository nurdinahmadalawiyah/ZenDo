package com.dinzio.zendo.features.settings.domain.usecase

import com.dinzio.zendo.features.settings.domain.repository.DriveSyncRepository
import javax.inject.Inject

class BackupToDriveUseCase @Inject constructor(
    private val repository: DriveSyncRepository
) {
    suspend operator fun invoke(jsonData: String, userEmail: String): Result<Unit> {
        return repository.backupToDrive(jsonData, userEmail)
    }
}