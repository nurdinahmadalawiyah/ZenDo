package com.dinzio.zendo.features.backup.presentation.viewModel

import android.content.Intent

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
    val pendingAction: BackupAction? = null
)
