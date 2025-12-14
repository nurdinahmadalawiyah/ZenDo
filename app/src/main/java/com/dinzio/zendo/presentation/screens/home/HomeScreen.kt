package com.dinzio.zendo.presentation.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.presentation.components.ZenDoCategoryCard
import com.dinzio.zendo.presentation.components.ZenDoCurrentTaskBanner
import com.dinzio.zendo.presentation.components.ZenDoInput
import com.dinzio.zendo.presentation.components.ZenDoSectionHeader
import com.dinzio.zendo.presentation.components.ZenDoTaskItemCard

@Composable
fun HomeScreen(
    isDarkTheme: Boolean = false,
    onThemeSwitch: (Boolean) -> Unit = {},
    onNavigateToDetail: () -> Unit = {},
    onNavigateToTimer: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        HomeTabletLayout(
            isDarkTheme = isDarkTheme,
            onThemeSwitch = onThemeSwitch,
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToTimer = onNavigateToTimer
        )
    } else {
        HomePhoneLayout(
            isDarkTheme = isDarkTheme,
            onThemeSwitch = onThemeSwitch,
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToTimer = onNavigateToTimer
        )
    }
}

// ==========================================
// 1. LAYOUT UNTUK PORTRAIT (HP Tegak)
// ==========================================
@Composable
fun HomePhoneLayout(
    isDarkTheme: Boolean,
    onThemeSwitch: (Boolean) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateToTimer: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            HeaderSection(
                searchQuery = searchQuery,
                onSearchChange = onSearchChange,
                isDarkTheme = isDarkTheme,
                onThemeSwitch = onThemeSwitch
            )

            Spacer(modifier = Modifier.height(24.dp))

            BannerSection(onNavigateToTimer)
            Spacer(modifier = Modifier.height(24.dp))

            CategorySection()

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            ZenDoSectionHeader(title = "Task List", onActionClick = {})
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(dummyTasks) { task ->
            ZenDoTaskItemCard(
                title = task.title,
                sessionCount = task.sessionCount,
                sessionDone = task.sessionDone,
                categoryIcon = task.icon,
                onItemClick = onNavigateToDetail,
                onPlayClick = onNavigateToTimer
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// ==========================================
// 2. LAYOUT UNTUK LANDSCAPE (Tablet / HP Miring)
// ==========================================
@Composable
fun HomeTabletLayout(
    isDarkTheme: Boolean,
    onThemeSwitch: (Boolean) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateToTimer: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(
                searchQuery = searchQuery,
                onSearchChange = onSearchChange,
                isDarkTheme = isDarkTheme,
                onThemeSwitch = onThemeSwitch
            )

            Spacer(modifier = Modifier.height(24.dp))

            BannerSection(onNavigateToTimer)
            Spacer(modifier = Modifier.height(24.dp))

            CategorySection()
        }

        Column(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
        ) {
            ZenDoSectionHeader(title = "Task List", onActionClick = {})
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(dummyTasks) { task ->
                    ZenDoTaskItemCard(
                        title = task.title,
                        sessionCount = task.sessionCount,
                        sessionDone = task.sessionDone,
                        categoryIcon = task.icon,
                        onItemClick = onNavigateToDetail,
                        onPlayClick = onNavigateToTimer
                    )
                }
            }
        }
    }
}

// ==========================================
// REUSABLE SECTIONS (Potongan UI)
// ==========================================

@Composable
fun HeaderSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    isDarkTheme: Boolean,
    onThemeSwitch: (Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ZenDo",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            IconButton(onClick = { onThemeSwitch(!isDarkTheme) }) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Switch Theme",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ZenDoInput(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = "Search Project...",
            leadingIcon = Icons.Default.Search
        )
    }
}

@Composable
fun BannerSection(onNavigateToTimer: () -> Unit) {
    Column {
        ZenDoCurrentTaskBanner(
            taskName = "Learn Angular",
            taskEmoji = "💻",
            sessionCount = "🎯 4 Sessions",
            sessionDone = "🔥 2 Done",
            onClick = {}
        )
    }
}

@Composable
fun CategorySection() {
    Column {
        ZenDoSectionHeader(title = "Categories", onActionClick = {})
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dummyCategories) { category ->
                ZenDoCategoryCard(
                    title = category.title,
                    taskCount = category.count,
                    icon = category.icon,
                    onClick = { /* Filter */ }
                )
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(name = "Portrait Phone", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewPortrait() {
    HomeScreen()
}

@Preview(
    name = "Landscape Mode",
    showBackground = true,
    device = "spec:width=800dp,height=411dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun PreviewLandscape() {
    HomeScreen()
}