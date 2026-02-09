package com.dinzio.zendo.features.settings.presentation.viewModel

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.R
import com.dinzio.zendo.core.data.local.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(@StringRes val resId: Int) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId)
        }
    }

    fun asString(context: android.content.Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId)
        }
    }
}

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupManager: BackupManager
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiText>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun performBackup(uri: Uri) {
        viewModelScope.launch {
            val result = backupManager.exportData(uri)
            if (result.isSuccess) {
                _uiEvent.emit(UiText.StringResource(R.string.backup_saved_successfully))
            } else {
                _uiEvent.emit(UiText.StringResource(R.string.failed_create_backup))
            }
        }
    }

    fun performRestore(uri: Uri) {
        viewModelScope.launch {
            val result = backupManager.importData(uri)
            if (result.isSuccess) {
                _uiEvent.emit(UiText.StringResource(R.string.data_recovered_successfully))
            } else {
                _uiEvent.emit(UiText.StringResource(R.string.the_backup_file_is_corrupted_or_invalid))
            }
        }
    }
}