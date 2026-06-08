package com.dinzio.zendo.features.backup.presentation.viewModel

import android.net.Uri

sealed class BackupEvent {
    // Local
    data class OnExportLocal(val uri: Uri) : BackupEvent()
    data class OnImportLocal(val uri: Uri) : BackupEvent()

    // Cloud
    data class OnBackupCloud(val userEmail: String) : BackupEvent()
    data class OnRestoreCloud(val userEmail: String) : BackupEvent()

    // Auth
    object OnAuthGranted : BackupEvent()
    object OnAuthDenied : BackupEvent()
    object ResetState : BackupEvent()
}
