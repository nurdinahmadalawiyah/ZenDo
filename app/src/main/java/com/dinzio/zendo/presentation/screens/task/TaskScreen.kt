package com.dinzio.zendo.presentation.screens.task

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.presentation.components.ZenDoCategoryCard
import com.dinzio.zendo.presentation.components.ZenDoInput
import com.dinzio.zendo.presentation.components.ZenDoTaskItemCard
import com.dinzio.zendo.presentation.components.ZenDoTopBar
import com.dinzio.zendo.presentation.screens.category.dummyCategoriesList
import com.dinzio.zendo.presentation.screens.home.dummyTasks

@Composable
fun TaskScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var searchQuery by remember { mutableStateOf("") }
    
    if (isLandscape) {
        TaskTabletLayout(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
    } else {
        TaskPhoneLayout(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
    }
}

@Composable
fun TaskPhoneLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ZenDoTopBar(
            title = "Tasks",
            actionIcon = Icons.Default.Add,
            onActionClick = { /* Navigate to Add Category */ },
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        ZenDoInput(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = "Search Task...",
            leadingIcon = Icons.Default.Search
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(dummyTasks) { task ->
                ZenDoTaskItemCard(
                    title = task.title,
                    sessionCount = task.sessionCount,
                    sessionDone = task.sessionDone,
                    categoryIcon = task.icon,
                    onItemClick = { },
                    onPlayClick = { }
                )
            }
        }
    }
}

@Composable
fun TaskTabletLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center content for Tablet
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        ZenDoTopBar(
            title = "Tasks",
            actionIcon = Icons.Default.Add,
            onActionClick = { /* Navigate to Add Category */ },
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.widthIn(max = 600.dp)) {
            ZenDoInput(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search Category...",
                leadingIcon = Icons.Default.Search
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(dummyTasks) { task ->
                ZenDoTaskItemCard(
                    title = task.title,
                    sessionCount = task.sessionCount,
                    sessionDone = task.sessionDone,
                    categoryIcon = task.icon,
                    onItemClick = { },
                    onPlayClick = { }
                )
            }
        }
    }
}

@Preview(name = "Phone", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewTaskPhoneLayout() {
    TaskScreen()
}

@Preview(name = "Tablet", showBackground = true, device = Devices.PIXEL_C)
@Composable
fun PreviewTaskTabletLayout() {
    TaskScreen()
}