package com.dinzio.zendo.features.backup.domain.usecase

import android.net.Uri
import com.dinzio.zendo.features.backup.domain.repository.BackupRepository
import javax.inject.Inject

class ImportLocalBackupUseCase @Inject constructor(
    private val repository: BackupRepository
) {
    suspend operator fun invoke(uri: Uri): Result<Unit> {
        return repository.importFromFile(uri)
    }
}