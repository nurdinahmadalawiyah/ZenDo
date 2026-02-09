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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Backup
import androidx.compose.material.icons.twotone.Restore
import androidx.compose.material.icons.twotone.Sync
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.features.settings.presentation.component.SettingsItem
import com.dinzio.zendo.features.settings.presentation.viewModel.BackupRestoreViewModel
import com.dinzio.zendo.features.settings.presentation.viewModel.UiText

@Composable
fun BackupRestoreSettingScreen(
    hideBackButton: Boolean = false,
    backupRestoreViewModel: BackupRestoreViewModel = hiltViewModel()
) {
    val context = LocalContext.current

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

    LaunchedEffect(Unit) {
        backupRestoreViewModel.uiEvent.collect { uiText ->
            val message = uiText.asString(context)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
                onClick = { }
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