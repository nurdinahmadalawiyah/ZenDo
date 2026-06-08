package com.dinzio.zendo.features.backup.domain.usecase

import com.dinzio.zendo.features.backup.domain.repository.BackupRepository
import javax.inject.Inject

class BackupToDriveUseCase @Inject constructor(
    private val repository: BackupRepository
) {
    suspend operator fun invoke(userEmail: String): Result<Unit> {
        return repository.backupToDrive(userEmail)
    }
}