package com.dinzio.zendo.features.backup.domain.usecase

import com.dinzio.zendo.features.backup.domain.model.BackupMetadata
import com.dinzio.zendo.features.backup.domain.repository.BackupRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveBackupMetadataUseCase @Inject constructor(
    private val repository: BackupRepository
) {
    operator fun invoke(): Flow<BackupMetadata> {
        return repository.observeBackupMetadata()
    }
}
