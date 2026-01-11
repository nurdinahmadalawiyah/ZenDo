package com.dinzio.zendo.features.settings.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.features.settings.presentation.component.SettingsCategoryTitle
import com.dinzio.zendo.features.settings.presentation.component.SettingsItem

@Composable
fun SettingsScreen(
    navController: NavController,
    currentThemeMode: String,
    onLanguageClick: () -> Unit = {},
) {
    val isLandscapeMode = isLandscape()

    if (isLandscapeMode) {
        SettingsTabletLayout()
    } else {
        SettingsPhoneLayout(
            navController = navController,
            currentThemeMode = currentThemeMode,
        )
    }

}

@Composable
fun SettingsPhoneLayout(
    navController: NavController,
    currentThemeMode: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ZenDoTopBar(
            title = "Settings",
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingsCategoryTitle(title = "General")

        SettingsItem(
            title = "Language",
            subtitle = "English",
            icon = Icons.TwoTone.Language,
            roundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            onClick = { navController.navigate(ZenDoRoutes.LanguageSetting.route) }
        )

        Spacer(modifier = Modifier.height(2.dp))

        SettingsItem(
            title = "Theme",
            subtitle = if (currentThemeMode == "dark") "Dark" else if (currentThemeMode == "light") "Light" else "System Default",
            icon = if (currentThemeMode == "dark") Icons.TwoTone.DarkMode else if (currentThemeMode == "light") Icons.TwoTone.LightMode else Icons.TwoTone.BrightnessAuto,
            roundedCornerShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            onClick = { navController.navigate(ZenDoRoutes.ThemeSetting.route) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingsCategoryTitle(title = "Timer Settings")

        SettingsItem(
            title = "Focus Duration",
            subtitle = "25 Minutes",
            icon = Icons.TwoTone.Timer,
            roundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            onClick = { /* Show duration picker */ }
        )

        Spacer(modifier = Modifier.height(2.dp))

        SettingsItem(
            title = "Auto-start Break",
            subtitle = "Enabled",
            icon = Icons.TwoTone.AutoMode,
            roundedCornerShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            onClick = { /* Show auto-start break settings */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingsCategoryTitle(title = "About")

        SettingsItem(
            title = "Version",
            subtitle = "1.0.0 (Build 2026)",
            icon = Icons.TwoTone.Info,
            onClick = { }
        )
    }
}

@Composable
fun SettingsTabletLayout() {
    TODO("Not yet implemented")
}



@Preview(name = "Portrait Phone", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewPortrait() {
    SettingsScreen(
        navController = rememberNavController(),
        currentThemeMode = "dark",
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
    )
}