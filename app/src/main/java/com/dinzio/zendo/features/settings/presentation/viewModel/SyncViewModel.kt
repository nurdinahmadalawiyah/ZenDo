package com.dinzio.zendo.features.settings.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.core.data.local.BackupManager
import com.dinzio.zendo.features.settings.domain.usecase.BackupToDriveUseCase
import com.dinzio.zendo.features.settings.domain.usecase.RestoreFromDriveUseCase
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val backupToDriveUseCase: BackupToDriveUseCase,
    private val restoreFromDriveUseCase: RestoreFromDriveUseCase,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _state = MutableStateFlow(SyncState())
    val state = _state.asStateFlow()

    private var tempUserEmail: String = ""

    fun onEvent(event: SyncEvent) {
        when (event) {
            is SyncEvent.OnBackupClick -> {
                tempUserEmail = event.userEmail
                executeBackup( tempUserEmail)
            }

            is SyncEvent.OnRestoreClick -> {
                tempUserEmail = event.userEmail
                executeRestore(tempUserEmail)
            }

            is SyncEvent.OnAuthGranted -> {
                _state.update { it.copy(authIntent = null) }
                if (_state.value.pendingAction == SyncAction.BACKUP) {
                    executeBackup(tempUserEmail)
                } else if (_state.value.pendingAction == SyncAction.RESTORE) {
                    executeRestore(tempUserEmail)
                }
            }

            is SyncEvent.OnAuthDenied -> {
                _state.update {
                    it.copy(
                        authIntent = null,
                        pendingAction = null,
                        isLoading = false,
                        error = "Permission denied"
                    )
                }
            }

            is SyncEvent.ResetState -> {
                _state.value = SyncState()
            }
        }
    }

    private fun executeBackup(email: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, successMessage = null) }

            try {
                val jsonString = backupManager.exportDataCloud()

                backupToDriveUseCase(jsonData = jsonString, userEmail = email).onSuccess {
                    _state.update { it.copy(
                        isLoading = false,
                        isSuccess = true,
                        pendingAction = null,
                        successMessage = "Pomodoro progress successfully backed up to the cloud."
                    ) }
                }.onFailure { e ->
                    handleFailure(e, SyncAction.BACKUP)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Failed to process local data: ${e.localizedMessage}") }
            }
        }
    }

    private fun executeRestore(email: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, restoredJsonData = null) }

            restoreFromDriveUseCase(email).onSuccess { jsonString ->
                val importResult = backupManager.importDataCloud(jsonString)
                if (importResult.isSuccess) {
                    _state.update { it.copy(
                        isLoading = false,
                        isSuccess = true,
                        pendingAction = null,
                        successMessage = "ZenDo data successfully recovered from Cloud!"
                    ) }
                } else {
                    _state.update { it.copy(
                        isLoading = false,
                        error = "Failed to insert data into device database."
                    ) }
                }
            }.onFailure { e ->
                handleFailure(e, SyncAction.RESTORE)
            }
        }
    }

    private fun handleFailure(
        e: Throwable,
        action: SyncAction
    ) {
        if (e is UserRecoverableAuthIOException) {
            _state.update {
                it.copy(
                    isLoading = false,
                    authIntent = e.intent,
                    pendingAction = action
                )
            }
        } else {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Error occurred"
                )
            }
        }
    }
}