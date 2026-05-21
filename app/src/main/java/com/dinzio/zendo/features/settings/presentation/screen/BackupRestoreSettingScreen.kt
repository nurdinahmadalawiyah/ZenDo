package com.dinzio.zendo.features.settings.presentation.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Backup
import androidx.compose.material.icons.twotone.Restore
import androidx.compose.material.icons.twotone.CloudDownload
import androidx.compose.material.icons.twotone.Sync
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.features.auth.presentation.viewModel.AuthViewModel
import com.dinzio.zendo.features.settings.presentation.component.SettingsItem
import com.dinzio.zendo.features.settings.presentation.viewModel.BackupRestoreViewModel
import com.dinzio.zendo.features.settings.presentation.viewModel.SyncEvent
import com.dinzio.zendo.features.settings.presentation.viewModel.SyncViewModel
import com.dinzio.zendo.features.settings.presentation.viewModel.UiText

@Composable
fun BackupRestoreSettingScreen(
    hideBackButton: Boolean = false,
    backupRestoreViewModel: BackupRestoreViewModel = hiltViewModel(),
    syncViewModel: SyncViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val syncState by syncViewModel.state.collectAsState()
    val authState by authViewModel.state.collectAsState()

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            syncViewModel.onEvent(SyncEvent.OnAuthGranted)
        } else {
            syncViewModel.onEvent(SyncEvent.OnAuthDenied)
        }
    }

    val createBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { backupRestoreViewModel.performBackup(it) }
    }

    val restoreBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { backupRestoreViewModel.performRestore(it) }
    }

    LaunchedEffect(syncState.authIntent) {
        syncState.authIntent?.let { intent ->
            authLauncher.launch(intent)
        }
    }

    LaunchedEffect(syncState.isSuccess, syncState.error) {
        if (syncState.isSuccess && syncState.successMessage != null) {
            Toast.makeText(context, syncState.successMessage, Toast.LENGTH_LONG).show()
            syncViewModel.onEvent(SyncEvent.ResetState)
        }
        if (syncState.error != null) {
            Toast.makeText(context, syncState.error, Toast.LENGTH_LONG).show()
            syncViewModel.onEvent(SyncEvent.ResetState)
        }
    }

    LaunchedEffect(Unit) {
        backupRestoreViewModel.uiEvent.collect { uiText ->
            val message = uiText.asString(context)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    val checkAuthAndRun = { action: (String) -> Unit ->
        val email = authState.user?.email
        if (email.isNullOrEmpty() || authState.user?.isAnonymous == true) {
            Toast.makeText(context, "Silakan hubungkan akun Google di menu Profil terlebih dahulu.", Toast.LENGTH_LONG).show()
        } else {
            action(email)
        }
    }

    val onBackupClick = {
        val fileName = "zendo_backup_${System.currentTimeMillis()}.json"
        createBackupLauncher.launch(fileName)
    }

    val onRestoreClick = {
        restoreBackupLauncher.launch(arrayOf("application/json"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ZenDoTopBar(
            title = stringResource(R.string.data_sync),
            isOnPrimaryBackground = true,
            hideBackButton = hideBackButton
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SettingsItem(
                title = stringResource(R.string.cloud_sync),
                subtitle = stringResource(R.string.sync_your_timer_settings_with_google_drive),
                icon = Icons.TwoTone.Sync,
                roundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                hideTrailing = true,
                onClick = {
                    checkAuthAndRun { email ->
                        syncViewModel.onEvent(SyncEvent.OnBackupClick(userEmail = email))
                    }
                }
            )

            SettingsItem(
                title = stringResource(R.string.restore_cloud),
                subtitle = stringResource(R.string.restore_your_timer_settings_from_google_drive),
                icon = Icons.TwoTone.CloudDownload,
                roundedCornerShape = RoundedCornerShape(0.dp),
                hideTrailing = true,
                onClick = {
                    checkAuthAndRun { email ->
                        syncViewModel.onEvent(SyncEvent.OnRestoreClick(userEmail = email))
                    }
                }
            )

            SettingsItem(
                title = stringResource(R.string.create_backup),
                subtitle = stringResource(R.string.export_your_data_as_a_json_backup_file),
                icon = Icons.TwoTone.Backup,
                roundedCornerShape = RoundedCornerShape(0.dp),
                hideTrailing = true,
                onClick = onBackupClick
            )

            SettingsItem(
                title = stringResource(R.string.restore_backup),
                subtitle = stringResource(R.string.import_a_backup_to_recover_your_settings),
                icon = Icons.TwoTone.Restore,
                roundedCornerShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                hideTrailing = true,
                onClick = onRestoreClick
            )
        }
    }
}