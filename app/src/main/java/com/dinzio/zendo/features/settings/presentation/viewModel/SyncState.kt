package com.dinzio.zendo.features.settings.presentation.viewModel

import android.content.Intent

enum class SyncAction {
    BACKUP,
    RESTORE
}

data class SyncState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null,
    val restoredJsonData: String? = null,
    val authIntent: Intent? = null,
    val pendingAction: SyncAction? = null
)