package com.dinzio.zendo.features.settings.presentation.screen

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
import androidx.compose.material.icons.twotone.BrightnessAuto
import androidx.compose.material.icons.twotone.DarkMode
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material.icons.twotone.LightMode
import androidx.compose.material.icons.twotone.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.R
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.features.settings.presentation.component.SettingsCategoryTitle
import com.dinzio.zendo.features.settings.presentation.component.SettingsItem

enum class SettingsPane {
    DEFAULT, LANGUAGE, THEME, FOCUS_TIMER, BREAK_TIMER
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
) {
    val isLandscapeMode = isLandscape()

    if (isLandscapeMode) {
        SettingsTabletLayout(
            currentThemeMode = currentThemeMode,
            currentLanguageCode = currentLanguageCode,
            currentFocusTime = currentFocusTime,
            currentBreakTime = currentBreakTime,
            onThemeChange = onThemeChange,
            onLanguageChange = onLanguageChange,
            onFocusTimeChange = onFocusTimeChange,
            onBreakTimeChange = onBreakTimeChange
        )
    } else {
        SettingsPhoneLayout(
            navController = navController,
            currentThemeMode = currentThemeMode,
            currentLanguageCode = currentLanguageCode,
            currentFocusTime = currentFocusTime,
            currentBreakTime = currentBreakTime
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
) {
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
        title = "Version",
        subtitle = "1.0.0 (Build 2026)",
        icon = Icons.TwoTone.Info,
        onClick = { }
    )
}

@Composable
fun SettingsPhoneLayout(
    navController: NavController,
    currentThemeMode: String,
    currentLanguageCode: String,
    currentFocusTime: Int,
    currentBreakTime: Int
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
            onBreakTimerClick = { navController.navigate(ZenDoRoutes.BreakTimerSetting.route) }
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
                onBreakTimerClick = { activePane = SettingsPane.BREAK_TIMER }
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