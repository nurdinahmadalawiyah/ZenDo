package com.dinzio.zendo.features.task.presentation.screen

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.core.presentation.components.ZenDoInput
import com.dinzio.zendo.core.presentation.components.ZenDoTaskItemCard
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.presentation.viewModel.TaskListViewModel

@Composable
fun TaskScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
) {
    val isLandscapeMode = isLandscape()

    val state by viewModel.state.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }

    val filteredTasks = state.tasks.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }
    
    if (isLandscapeMode) {
        TaskTabletLayout(
            tasks = filteredTasks,
            isLoading = state.isLoading,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
    } else {
        TaskPhoneLayout(
            tasks = filteredTasks,
            isLoading = state.isLoading,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
    }
}

@Composable
fun TaskPhoneLayout(
    tasks: List<TaskModel>,
    isLoading: Boolean,
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

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(tasks) { task ->
                    ZenDoTaskItemCard(
                        title = task.title,
                        sessionCount = task.sessionCount.toString(),
                        sessionDone = task.sessionDone.toString(),
                        categoryIcon = task.icon,
                        onItemClick = { },
                        onPlayClick = { }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskTabletLayout(
    tasks: List<TaskModel>,
    isLoading: Boolean,
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

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(tasks) { task ->
                    ZenDoTaskItemCard(
                        title = task.title,
                        sessionCount = task.sessionCount.toString(),
                        sessionDone = task.sessionDone.toString(),
                        categoryIcon = task.icon,
                        onItemClick = { },
                        onPlayClick = { }
                    )
                }
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