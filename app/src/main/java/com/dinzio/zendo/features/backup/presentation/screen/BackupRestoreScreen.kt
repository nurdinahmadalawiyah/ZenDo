package com.dinzio.zendo.features.backup.presentation.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Backup
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Cloud
import androidx.compose.material.icons.twotone.CloudDownload
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material.icons.twotone.RadioButtonUnchecked
import androidx.compose.material.icons.twotone.Restore
import androidx.compose.material.icons.twotone.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoButton
import com.dinzio.zendo.features.backup.domain.model.BackupMetadata
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.theme.GreenAccent
import com.dinzio.zendo.core.theme.GreenPrimary
import com.dinzio.zendo.core.theme.OrangeAccent
import com.dinzio.zendo.features.auth.presentation.viewModel.AuthViewModel
import com.dinzio.zendo.features.backup.presentation.viewModel.BackupAction
import com.dinzio.zendo.features.backup.presentation.viewModel.BackupEvent
import com.dinzio.zendo.features.backup.presentation.viewModel.BackupViewModel
import com.dinzio.zendo.features.settings.presentation.component.SettingsItem
import java.text.DateFormat

enum class BackupScreenRoute {
    Local,
    Cloud
}

@Composable
fun BackupRestoreScreen(
    titleRoute: BackupScreenRoute = BackupScreenRoute.Local,
    onOpenCloudBackup: (() -> Unit)? = null,
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

    val showCloudActions = titleRoute == BackupScreenRoute.Cloud
    val showLocalActions = titleRoute == BackupScreenRoute.Local
    val canAccessCloudBackup = !authState.user?.email.isNullOrEmpty() && authState.user?.isAnonymous != true
    val screenTitle = if (showCloudActions) {
        stringResource(R.string.cloud_sync)
    } else {
        stringResource(R.string.data_sync)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .verticalScroll(androidx.compose.foundation.rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ZenDoTopBar(
            title = screenTitle,
            isOnPrimaryBackground = true,
            hideBackButton = hideBackButton
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (showCloudActions) {
            if (canAccessCloudBackup) {
                CloudBackupContent(
                    metadata = backupState.metadata,
                    isLoading = backupState.isLoading,
                    pendingAction = backupState.pendingAction,
                    onBackupClick = {
                        checkAuthAndRun { email ->
                            backupViewModel.onEvent(BackupEvent.OnBackupCloud(userEmail = email))
                        }
                    },
                    onRestoreClick = {
                        checkAuthAndRun { email ->
                            backupViewModel.onEvent(BackupEvent.OnRestoreCloud(userEmail = email))
                        }
                    }
                )
            } else {
                CloudBackupLockedCard(message = connectText)
            }
        }

        if (showLocalActions) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                SettingsItem(
                    title = stringResource(R.string.cloud_sync),
                    subtitle = if (canAccessCloudBackup) {
                        stringResource(R.string.sync_your_timer_settings_with_google_drive)
                    } else {
                        connectText
                    },
                    icon = Icons.TwoTone.Sync,
                    roundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    hideTrailing = onOpenCloudBackup == null,
                    enabled = canAccessCloudBackup,
                    onClick = { onOpenCloudBackup?.invoke() }
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
}

@Composable
private fun CloudBackupContent(
    metadata: BackupMetadata,
    isLoading: Boolean,
    pendingAction: BackupAction?,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit
) {
    CloudHeroCard()
    Spacer(modifier = Modifier.height(16.dp))
    BackupMetadataCard(metadata = metadata)
    Spacer(modifier = Modifier.height(16.dp))

    if (isLoading) {
        CloudLoadingCard(action = pendingAction)
        Spacer(modifier = Modifier.height(16.dp))
    }

    CloudActionCard(
        title = stringResource(R.string.cloud_sync),
        subtitle = stringResource(R.string.sync_your_timer_settings_with_google_drive),
        buttonText = stringResource(R.string.cloud_sync),
        icon = Icons.TwoTone.Cloud,
        accentColor = GreenAccent,
        enabled = !isLoading,
        onClick = onBackupClick
    )

    Spacer(modifier = Modifier.height(12.dp))

    CloudActionCard(
        title = stringResource(R.string.restore_cloud),
        subtitle = stringResource(R.string.restore_your_timer_settings_from_google_drive),
        buttonText = stringResource(R.string.restore_cloud),
        icon = Icons.TwoTone.CloudDownload,
        accentColor = OrangeAccent,
        enabled = !isLoading,
        onClick = onRestoreClick
    )

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun CloudBackupLockedCard(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.TwoTone.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = stringResource(R.string.cloud_backup_locked_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CloudHeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Cloud,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                StatusPill()
            }

            Text(
                text = stringResource(R.string.cloud_backup_hero_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.cloud_backup_hero_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun StatusPill() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.16f))
            .border(
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.TwoTone.CheckCircle,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = stringResource(R.string.cloud_backup_status_badge),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

@Composable
private fun CloudActionCard(
    title: String,
    subtitle: String,
    buttonText: String,
    icon: ImageVector,
    accentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.18f)),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(accentColor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        ZenDoButton(
            text = buttonText,
            onClick = onClick,
            enabled = enabled,
            containerColor = accentColor,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun CloudLoadingCard(action: BackupAction?) {
    val message = when (action) {
        BackupAction.BACKUP_CLOUD -> stringResource(R.string.backing_up_to_cloud)
        BackupAction.RESTORE_CLOUD -> stringResource(R.string.restoring_from_cloud)
        else -> stringResource(R.string.processing_cloud_backup)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator()
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
private fun BackupMetadataCard(metadata: BackupMetadata) {
    val lastBackupText = metadata.lastBackupTimestamp?.let {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(it)
    } ?: stringResource(R.string.backup_metadata_never)
    val backupSizeText = metadata.lastBackupSizeBytes?.toReadableSize()
        ?: stringResource(R.string.backup_metadata_unknown)
    val statusPrefix = when (metadata.lastStatusSuccess) {
        true -> stringResource(R.string.backup_status_success)
        false -> stringResource(R.string.backup_status_failed)
        null -> stringResource(R.string.backup_metadata_unknown)
    }
    val operationText = metadata.lastOperation?.toOperationLabel()
    val statusSuffix = metadata.lastStatusMessage ?: stringResource(R.string.backup_metadata_unknown)
    val statusText = listOfNotNull(operationText, "$statusPrefix: $statusSuffix").joinToString(" • ")
    val statusColor = when (metadata.lastStatusSuccess) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.error
        null -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = stringResource(R.string.cloud_backup_activity_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetadataChip(
                modifier = Modifier.weight(1f),
                icon = Icons.TwoTone.History,
                label = stringResource(R.string.last_backup_label),
                value = lastBackupText
            )
            MetadataChip(
                modifier = Modifier.weight(1f),
                icon = Icons.TwoTone.Inventory2,
                label = stringResource(R.string.backup_size_label),
                value = backupSizeText
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(statusColor.copy(alpha = 0.12f))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (metadata.lastStatusSuccess == true) {
                    Icons.TwoTone.CheckCircle
                } else {
                    Icons.TwoTone.RadioButtonUnchecked
                },
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(20.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.last_status_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun MetadataChip(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.55f))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun String.toOperationLabel(): String {
    return when (this) {
        "CLOUD_BACKUP" -> stringResource(R.string.backup_operation_cloud_backup)
        "CLOUD_RESTORE" -> stringResource(R.string.backup_operation_cloud_restore)
        "LOCAL_BACKUP" -> stringResource(R.string.backup_operation_local_backup)
        "LOCAL_RESTORE" -> stringResource(R.string.backup_operation_local_restore)
        else -> this
    }
}

private fun Long.toReadableSize(): String {
    val kilobytes = this / 1024.0
    return if (kilobytes < 1024) {
        String.format("%.1f KB", kilobytes)
    } else {
        String.format("%.2f MB", kilobytes / 1024.0)
    }
}
