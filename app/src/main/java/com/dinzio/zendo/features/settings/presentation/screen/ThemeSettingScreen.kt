package com.dinzio.zendo.features.settings.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar

@Composable
fun ThemeSettingScreen(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    hideBackButton: Boolean = false
) {
    val options = listOf(
        stringResource(R.string.light_mode) to "light",
        stringResource(R.string.dark_mode) to "dark",
        stringResource(R.string.system_default) to "system"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ZenDoTopBar(
            title = stringResource(R.string.select_theme),
            isOnPrimaryBackground = true,
            hideBackButton = hideBackButton
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            options.forEachIndexed { index, (label, value) ->
                val shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    options.size - 1 -> RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    else -> RoundedCornerShape(0.dp)
                }

                ListItem(
                    headlineContent = { Text(label) },
                    trailingContent = {
                        RadioButton(
                            selected = (currentTheme == value),
                            onClick = { onThemeSelected(value) }
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .clickable { onThemeSelected(value) }
                )
            }
        }
    }
}