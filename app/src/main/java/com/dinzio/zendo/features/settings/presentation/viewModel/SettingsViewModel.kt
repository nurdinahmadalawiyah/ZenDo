package com.dinzio.zendo.features.settings.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.core.data.local.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupManager: BackupManager
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun performBackup(uri: Uri) {
        viewModelScope.launch {
            val result = backupManager.exportData(uri)
            if (result.isSuccess) {
                _uiEvent.emit("Backup berhasil disimpan!")
            } else {
                _uiEvent.emit("Gagal membuat backup.")
            }
        }
    }

    fun performRestore(uri: Uri) {
        viewModelScope.launch {
            val result = backupManager.importData(uri)
            if (result.isSuccess) {
                _uiEvent.emit("Data berhasil dipulihkan! Silakan restart aplikasi.")
            } else {
                _uiEvent.emit("File backup rusak atau tidak valid.")
            }
        }
    }
}