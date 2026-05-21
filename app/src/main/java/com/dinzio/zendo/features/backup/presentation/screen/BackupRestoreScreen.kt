package com.dinzio.zendo.features.backup.presentation.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Backup
import androidx.compose.material.icons.twotone.CloudDownload
import androidx.compose.material.icons.twotone.Restore
import androidx.compose.material.icons.twotone.Sync
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.features.auth.presentation.viewModel.AuthViewModel
import com.dinzio.zendo.features.backup.presentation.viewModel.BackupEvent
import com.dinzio.zendo.features.backup.presentation.viewModel.BackupViewModel
import com.dinzio.zendo.features.settings.presentation.component.SettingsItem

@Composable
fun BackupRestoreScreen(
    hideBackButton: Boolean = false,
    backupViewModel: BackupViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val backupState by backupViewModel.state.collectAsState()
    val authState by authViewModel.state.collectAsState()

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            backupViewModel.onEvent(BackupEvent.OnAuthGranted)
        } else {
            backupViewModel.onEvent(BackupEvent.OnAuthDenied)
        }
    }

    val createBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { backupViewModel.onEvent(BackupEvent.OnExportLocal(it)) }
    }

    val restoreBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { backupViewModel.onEvent(BackupEvent.OnImportLocal(it)) }
    }

    LaunchedEffect(backupState.authIntent) {
        backupState.authIntent?.let { intent ->
            authLauncher.launch(intent)
        }
    }

    LaunchedEffect(backupState.isSuccess, backupState.error) {
        if (backupState.isSuccess && backupState.successMessage != null) {
            Toast.makeText(context, backupState.successMessage, Toast.LENGTH_LONG).show()
            backupViewModel.onEvent(BackupEvent.ResetState)
        }
        if (backupState.error != null) {
            Toast.makeText(context, backupState.error, Toast.LENGTH_LONG).show()
            backupViewModel.onEvent(BackupEvent.ResetState)
        }
    }

    LaunchedEffect(Unit) {
        backupViewModel.uiEvent.collect { uiText ->
            val message = uiText.asString(context)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    val connectText =
        stringResource(R.string.please_connect_your_google_account_in_the_profile_menu_first)

    val checkAuthAndRun = { action: (String) -> Unit ->
        val email = authState.user?.email
        if (email.isNullOrEmpty() || authState.user?.isAnonymous == true) {
            Toast.makeText(context, connectText, Toast.LENGTH_LONG).show()
        } else {
            action(email)
        }
    }

    val onLocalBackupClick = {
        val fileName = "zendo_backup_${System.currentTimeMillis()}.json"
        createBackupLauncher.launch(fileName)
    }

    val onLocalRestoreClick = {
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
                        backupViewModel.onEvent(BackupEvent.OnBackupCloud(userEmail = email))
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
                        backupViewModel.onEvent(BackupEvent.OnRestoreCloud(userEmail = email))
                    }
                }
            )

            SettingsItem(
                title = stringResource(R.string.create_backup),
                subtitle = stringResource(R.string.export_your_data_as_a_json_backup_file),
                icon = Icons.TwoTone.Backup,
                roundedCornerShape = RoundedCornerShape(0.dp),
                hideTrailing = true,
                onClick = onLocalBackupClick
            )

            SettingsItem(
                title = stringResource(R.string.restore_backup),
                subtitle = stringResource(R.string.import_a_backup_to_recover_your_settings),
                icon = Icons.TwoTone.Restore,
                roundedCornerShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                hideTrailing = true,
                onClick = onLocalRestoreClick
            )
        }
    }
}
