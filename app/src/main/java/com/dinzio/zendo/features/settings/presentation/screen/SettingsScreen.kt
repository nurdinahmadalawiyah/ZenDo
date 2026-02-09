package com.dinzio.zendo.features.settings.presentation.screen

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AutoMode
import androidx.compose.material.icons.twotone.Backup
import androidx.compose.material.icons.twotone.BrightnessAuto
import androidx.compose.material.icons.twotone.DarkMode
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.LightMode
import androidx.compose.material.icons.twotone.Restore
import androidx.compose.material.icons.twotone.Sync
import androidx.compose.material.icons.twotone.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.R
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.features.settings.presentation.component.SettingsCategoryTitle
import com.dinzio.zendo.features.settings.presentation.component.SettingsItem
import com.dinzio.zendo.features.settings.presentation.viewModel.SettingsViewModel

enum class SettingsPane {
    DEFAULT, LANGUAGE, THEME, FOCUS_TIMER, BREAK_TIMER, VERSION
}

@Composable
fun SettingsScreen(
    navController: NavController,
    currentThemeMode: String,
    currentLanguageCode: String,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    currentFocusTime: Int,
    onFocusTimeChange: (Int) -> Unit,
    currentBreakTime: Int,
    onBreakTimeChange: (Int) -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLandscapeMode = isLandscape()

    val createBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { settingsViewModel.performBackup(it) }
    }

    val restoreBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { settingsViewModel.performRestore(it) }
    }

    LaunchedEffect(Unit) {
        settingsViewModel.uiEvent.collect { message ->
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

    if (isLandscapeMode) {
        SettingsTabletLayout(
            currentThemeMode = currentThemeMode,
            currentLanguageCode = currentLanguageCode,
            currentFocusTime = currentFocusTime,
            currentBreakTime = currentBreakTime,
            onThemeChange = onThemeChange,
            onLanguageChange = onLanguageChange,
            onFocusTimeChange = onFocusTimeChange,
            onBreakTimeChange = onBreakTimeChange,
            onBackupClick = onBackupClick,
            onRestoreClick = onRestoreClick
        )
    } else {
        SettingsPhoneLayout(
            navController = navController,
            currentThemeMode = currentThemeMode,
            currentLanguageCode = currentLanguageCode,
            currentFocusTime = currentFocusTime,
            currentBreakTime = currentBreakTime,
            onBackupClick = onBackupClick,
            onRestoreClick = onRestoreClick
        )
    }
}

@Composable
private fun SettingsListContent(
    currentThemeMode: String,
    currentLanguageCode: String,
    currentFocusTime: Int,
    currentBreakTime: Int,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onFocusTimerClick: () -> Unit,
    onBreakTimerClick: () -> Unit,
    onVersionClick: () -> Unit,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    val context = LocalContext.current
    val packageInfo = remember(context) {
        context.packageManager.getPackageInfo(context.packageName, 0)
    }
    val versionName = remember(context) {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode.toLong()
    }

    SettingsCategoryTitle(title = stringResource(R.string.general))

    SettingsItem(
        title = stringResource(R.string.language),
        subtitle = if (currentLanguageCode == "en") stringResource(R.string.english)
        else if (currentLanguageCode == "in") stringResource(R.string.bahasa_indonesia)
        else stringResource(R.string.system_default),
        icon = Icons.TwoTone.Language,
        roundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        onClick = onLanguageClick
    )

    Spacer(modifier = Modifier.height(2.dp))

    SettingsItem(
        title = stringResource(R.string.theme),
        subtitle = if (currentThemeMode == "dark") stringResource(R.string.dark_mode)
        else if (currentThemeMode == "light") stringResource(R.string.light_mode)
        else stringResource(R.string.system_default),
        icon = if (currentThemeMode == "dark") Icons.TwoTone.DarkMode
        else if (currentThemeMode == "light") Icons.TwoTone.LightMode
        else Icons.TwoTone.BrightnessAuto,
        roundedCornerShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        onClick = onThemeClick
    )

    Spacer(modifier = Modifier.height(24.dp))

    SettingsCategoryTitle(title = stringResource(R.string.data_sync))

    SettingsItem(
        title = stringResource(R.string.cloud_sync),
        subtitle = stringResource(R.string.sync_your_timer_settings_with_google_drive),
        icon = Icons.TwoTone.Sync,
        roundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        onClick = { }
    )

    Spacer(modifier = Modifier.height(2.dp))

    SettingsItem(
        title = stringResource(R.string.create_backup),
        subtitle = stringResource(R.string.export_your_data_as_a_json_backup_file),
        icon = Icons.TwoTone.Backup,
        roundedCornerShape = RoundedCornerShape(0.dp),
        onClick = onBackupClick
    )

    Spacer(modifier = Modifier.height(2.dp))

    SettingsItem(
        title = stringResource(R.string.restore_backup),
        subtitle = stringResource(R.string.import_a_backup_to_recover_your_settings),
        icon = Icons.TwoTone.Restore,
        roundedCornerShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        onClick = onRestoreClick
    )

    Spacer(modifier = Modifier.height(24.dp))

    SettingsCategoryTitle(title = stringResource(R.string.timer_settings))

    SettingsItem(
        title = stringResource(R.string.focus_duration),
        subtitle = stringResource(R.string.minutes, currentFocusTime),
        icon = Icons.TwoTone.Timer,
        roundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        onClick = onFocusTimerClick
    )

    Spacer(modifier = Modifier.height(2.dp))

    SettingsItem(
        title = stringResource(R.string.break_duration),
        subtitle = stringResource(R.string.minutes, currentBreakTime),
        icon = Icons.TwoTone.AutoMode,
        roundedCornerShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        onClick = onBreakTimerClick
    )

    Spacer(modifier = Modifier.height(24.dp))

    SettingsCategoryTitle(title = stringResource(R.string.about))

    SettingsItem(
        title = stringResource(R.string.version),
        subtitle = "$versionName (Build ${versionCode})",
        icon = Icons.TwoTone.Info,
        onClick = onVersionClick
    )
}

@Composable
fun SettingsPhoneLayout(
    navController: NavController,
    currentThemeMode: String,
    currentLanguageCode: String,
    currentFocusTime: Int,
    currentBreakTime: Int,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ZenDoTopBar(
            title = stringResource(R.string.settings),
            isOnPrimaryBackground = true,
            hideBackButton = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        SettingsListContent(
            currentThemeMode = currentThemeMode,
            currentLanguageCode = currentLanguageCode,
            currentFocusTime = currentFocusTime,
            currentBreakTime = currentBreakTime,
            onLanguageClick = { navController.navigate(ZenDoRoutes.LanguageSetting.route) },
            onThemeClick = { navController.navigate(ZenDoRoutes.ThemeSetting.route) },
            onFocusTimerClick = { navController.navigate(ZenDoRoutes.FocusTimerSetting.route) },
            onBreakTimerClick = { navController.navigate(ZenDoRoutes.BreakTimerSetting.route) },
            onVersionClick = { navController.navigate(ZenDoRoutes.VersionSetting.route) },
            onBackupClick = onBackupClick,
            onRestoreClick = onRestoreClick
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SettingsTabletLayout(
    currentThemeMode: String,
    currentLanguageCode: String,
    currentFocusTime: Int,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    currentBreakTime: Int,
    onBreakTimeChange: (Int) -> Unit,
    onFocusTimeChange: (Int) -> Unit,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    var activePane by remember { mutableStateOf(SettingsPane.DEFAULT) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ZenDoTopBar(
                title = stringResource(R.string.settings),
                isOnPrimaryBackground = true,
                hideBackButton = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            SettingsListContent(
                currentThemeMode = currentThemeMode,
                currentLanguageCode = currentLanguageCode,
                currentFocusTime = currentFocusTime,
                currentBreakTime = currentBreakTime,
                onLanguageClick = { activePane = SettingsPane.LANGUAGE },
                onThemeClick = { activePane = SettingsPane.THEME },
                onFocusTimerClick = { activePane = SettingsPane.FOCUS_TIMER },
                onBreakTimerClick = { activePane = SettingsPane.BREAK_TIMER },
                onVersionClick = { activePane = SettingsPane.VERSION },
                onBackupClick = onBackupClick,
                onRestoreClick = onRestoreClick
            )
        }

        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
        ) {
            when (activePane) {
                SettingsPane.DEFAULT -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.TwoTone.Info,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.material3.Text(
                            text = stringResource(R.string.select_menu_on_the_left_to_configure_the_app),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                SettingsPane.LANGUAGE -> {
                    LanguageSettingScreen(
                        currentLocale = currentLanguageCode,
                        onLanguageSelected = onLanguageChange,
                        hideBackButton = true
                    )
                }

                SettingsPane.THEME -> {
                    ThemeSettingScreen(
                        currentTheme = currentThemeMode,
                        onThemeSelected = onThemeChange,
                        hideBackButton = true
                    )
                }

                SettingsPane.FOCUS_TIMER -> {
                    FocusTimerSettingScreen(
                        currentFocusTime = currentFocusTime,
                        onFocusTimeSelected = onFocusTimeChange,
                        hideBackButton = true
                    )
                }

                SettingsPane.BREAK_TIMER -> {
                    BreakTimerSettingScreen(
                        currentBreakTime = currentBreakTime,
                        onBreakTimeSelected = onBreakTimeChange,
                        hideBackButton = true
                    )
                }

                SettingsPane.VERSION -> {
                    VersionSettingScreen(
                        hideBackButton = true
                    )
                }
            }
        }
    }
}

@Preview(name = "Portrait Phone", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewPortrait() {
    SettingsScreen(
        navController = rememberNavController(),
        currentThemeMode = "dark",
        currentLanguageCode = "en",
        currentFocusTime = 25,
        currentBreakTime = 5,
        onThemeChange = {},
        onLanguageChange = {},
        onFocusTimeChange = {},
        onBreakTimeChange = {}
    )
}

@Preview(
    name = "Landscape Mode",
    showBackground = true,
    device = "spec:width=800dp,height=411dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun PreviewLandscape() {
    SettingsScreen(
        navController = rememberNavController(),
        currentThemeMode = "dark",
        currentLanguageCode = "en",
        currentFocusTime = 25,
        currentBreakTime = 5,
        onThemeChange = {},
        onLanguageChange = {},
        onFocusTimeChange = {},
        onBreakTimeChange = {}
    )
}