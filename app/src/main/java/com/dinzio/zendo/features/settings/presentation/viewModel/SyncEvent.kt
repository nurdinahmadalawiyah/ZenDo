package com.dinzio.zendo.features.settings.presentation.viewModel

sealed class SyncEvent {
    data class OnBackupClick(val userEmail: String) : SyncEvent()
    data class OnRestoreClick(val userEmail: String) : SyncEvent()
    object OnAuthGranted : SyncEvent()
    object OnAuthDenied : SyncEvent()
    object ResetState : SyncEvent()
}