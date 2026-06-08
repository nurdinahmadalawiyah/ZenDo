package com.dinzio.zendo.features.backup.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.R
import com.dinzio.zendo.core.util.UiText
import com.dinzio.zendo.features.auth.domain.usecase.ObserveUserUseCase
import com.dinzio.zendo.features.backup.domain.usecase.BackupToDriveUseCase
import com.dinzio.zendo.features.backup.domain.usecase.ExportLocalBackupUseCase
import com.dinzio.zendo.features.backup.domain.usecase.ImportLocalBackupUseCase
import com.dinzio.zendo.features.backup.domain.usecase.ObserveBackupMetadataUseCase
import com.dinzio.zendo.features.backup.domain.usecase.RestoreFromDriveUseCase
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val observeUserUseCase: ObserveUserUseCase,
    private val exportLocalBackupUseCase: ExportLocalBackupUseCase,
    private val importLocalBackupUseCase: ImportLocalBackupUseCase,
    private val backupToDriveUseCase: BackupToDriveUseCase,
    private val restoreFromDriveUseCase: RestoreFromDriveUseCase,
    private val observeBackupMetadataUseCase: ObserveBackupMetadataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BackupState())
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiText>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var tempUserEmail: String = ""
    private var metadataJob: Job? = null
    private var observedMetadataEmail: String? = null

    init {
        observeActiveUser()
    }

    fun onEvent(event: BackupEvent) {
        when (event) {
            is BackupEvent.OnExportLocal -> performLocalBackup(event.uri)
            is BackupEvent.OnImportLocal -> performLocalRestore(event.uri)

            is BackupEvent.OnBackupCloud -> {
                tempUserEmail = event.userEmail
                executeCloudBackup(tempUserEmail)
            }

            is BackupEvent.OnRestoreCloud -> {
                tempUserEmail = event.userEmail
                executeCloudRestore(tempUserEmail)
            }

            is BackupEvent.OnAuthGranted -> {
                _state.update { it.copy(authIntent = null) }
                when (_state.value.pendingAction) {
                    BackupAction.BACKUP_CLOUD -> executeCloudBackup(tempUserEmail)
                    BackupAction.RESTORE_CLOUD -> executeCloudRestore(tempUserEmail)
                    else -> {}
                }
            }

            is BackupEvent.OnAuthDenied -> {
                _state.update {
                    it.copy(
                        authIntent = null,
                        pendingAction = null,
                        isLoading = false,
                        error = "Permission denied"
                    )
                }
            }

            is BackupEvent.ResetState -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        successMessage = null,
                        error = null,
                        authIntent = null,
                        pendingAction = null
                    )
                }
            }
        }
    }

    private fun observeActiveUser() {
        viewModelScope.launch {
            observeUserUseCase().collect { user ->
                val userEmail = user?.takeUnless { it.isAnonymous }?.email
                if (observedMetadataEmail != userEmail) {
                    observedMetadataEmail = userEmail
                    observeBackupMetadata(userEmail)
                }
            }
        }
    }

    private fun observeBackupMetadata(userEmail: String?) {
        metadataJob?.cancel()
        metadataJob = viewModelScope.launch {
            observeBackupMetadataUseCase(userEmail).collect { metadata ->
                _state.update { it.copy(metadata = metadata) }
            }
        }
    }

    private fun performLocalBackup(uri: Uri) {
        viewModelScope.launch {
            val result = exportLocalBackupUseCase(uri)
            if (result.isSuccess) {
                _uiEvent.emit(UiText.StringResource(R.string.backup_saved_successfully))
            } else {
                _uiEvent.emit(UiText.StringResource(R.string.failed_create_backup))
            }
        }
    }

    private fun performLocalRestore(uri: Uri) {
        viewModelScope.launch {
            val result = importLocalBackupUseCase(uri)
            if (result.isSuccess) {
                _uiEvent.emit(UiText.StringResource(R.string.data_recovered_successfully))
            } else {
                _uiEvent.emit(UiText.StringResource(R.string.the_backup_file_is_corrupted_or_invalid))
            }
        }
    }

    private fun executeCloudBackup(email: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null,
                    pendingAction = BackupAction.BACKUP_CLOUD
                )
            }

            try {
                backupToDriveUseCase(email).onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            pendingAction = null,
                            successMessage = "Pomodoro progress successfully backed up to the cloud."
                        )
                    }
                }.onFailure { e ->
                    handleCloudFailure(e, BackupAction.BACKUP_CLOUD)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to process local data: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    private fun executeCloudRestore(email: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    pendingAction = BackupAction.RESTORE_CLOUD
                )
            }

            restoreFromDriveUseCase(email).onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        pendingAction = null,
                        successMessage = "ZenDo data successfully recovered from Cloud!"
                    )
                }
            }.onFailure { e ->
                handleCloudFailure(e, BackupAction.RESTORE_CLOUD)
            }
        }
    }

    private fun handleCloudFailure(e: Throwable, action: BackupAction) {
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
