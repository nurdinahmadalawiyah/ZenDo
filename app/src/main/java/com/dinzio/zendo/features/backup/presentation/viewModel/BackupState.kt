package com.dinzio.zendo.features.backup.presentation.viewModel

import android.content.Intent
import com.dinzio.zendo.features.backup.domain.model.BackupMetadata

enum class BackupAction {
    EXPORT_LOCAL,
    IMPORT_LOCAL,
    BACKUP_CLOUD,
    RESTORE_CLOUD
}

data class BackupState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null,
    val authIntent: Intent? = null,
    val pendingAction: BackupAction? = null,
    val metadata: BackupMetadata = BackupMetadata()
)
