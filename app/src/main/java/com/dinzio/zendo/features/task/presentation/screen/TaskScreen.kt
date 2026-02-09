package com.dinzio.zendo.features.task.presentation.screen

import ZenDoEmptyState
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
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.R
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.core.presentation.components.ZenDoActionSheet
import com.dinzio.zendo.core.presentation.components.ZenDoConfirmDialog
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.core.presentation.components.ZenDoInput
import com.dinzio.zendo.core.presentation.components.ZenDoTaskItemCard
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionEvent
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionViewModel
import com.dinzio.zendo.features.task.presentation.viewModel.taskList.TaskListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navController: NavHostController,
    viewModel: TaskListViewModel = hiltViewModel(),
    actionViewModel: TaskActionViewModel = hiltViewModel()
) {
    val isLandscapeMode = isLandscape()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val actionState by actionViewModel.state.collectAsStateWithLifecycle()

    var selectedTask by remember { mutableStateOf<TaskModel?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showActionSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var searchQuery by remember { mutableStateOf("") }

    val filteredTasks = state.tasks.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(actionState.isSuccess) {
        if (actionState.isSuccess) {
            showActionSheet = false
            showDeleteDialog = false
            selectedTask = null
            actionViewModel.onEvent(TaskActionEvent.OnResetTask)
        }
    }

    if (showActionSheet && selectedTask != null) {
        ZenDoActionSheet(
            title = selectedTask?.title ?: "",
            icon = selectedTask?.icon ?: "",
            sheetState = sheetState,
            onDismiss = { showActionSheet = false },
            onEditClick = {
                showActionSheet = false
                navController.navigate(ZenDoRoutes.EditTask.passId(selectedTask!!.id))
            },
            onDeleteClick = { showDeleteDialog = true }
        )
    }

    if (showDeleteDialog && selectedTask != null) {
        selectedTask?.title?.let {
            ZenDoConfirmDialog(
                title = stringResource(R.string.delete_task),
                message = stringResource(
                    R.string.are_you_sure_you_want_to_delete_this_action_cannot_be_undone,
                    it
                ),
                confirmText = stringResource(R.string.delete),
                dismissText = stringResource(R.string.cancel),
                onConfirm = {
                    actionViewModel.onEvent(TaskActionEvent.OnDeleteTask(selectedTask!!))
                },
                onDismiss = {
                    showDeleteDialog = false
                },
                isLoading = actionState.isDeleting
            )
        }
    }

    if (isLandscapeMode) {
        TaskTabletLayout(
            navController = navController,
            tasks = filteredTasks,
            isLoading = state.isLoading,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onLongItemClick = { task ->
                selectedTask = task
                showActionSheet = true
            },
            onNavigateToAdd = { navController.navigate(ZenDoRoutes.AddTask.route) }
        )
    } else {
        TaskPhoneLayout(
            navController = navController,
            tasks = filteredTasks,
            isLoading = state.isLoading,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onLongItemClick = { task ->
                selectedTask = task
                showActionSheet = true
            },
            onNavigateToAdd = { navController.navigate(ZenDoRoutes.AddTask.route) }
        )
    }
}

@Composable
fun TaskPhoneLayout(
    navController: NavController,
    tasks: List<TaskModel>,
    isLoading: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onLongItemClick: (TaskModel) -> Unit,
    onNavigateToAdd: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ZenDoTopBar(
            title = stringResource(R.string.tasks),
            actionIcon = Icons.Default.Add,
            onActionClick = onNavigateToAdd,
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        ZenDoInput(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = stringResource(R.string.search_task),
            leadingIcon = Icons.Default.Search
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (tasks.isEmpty()) {
            ZenDoEmptyState(
                text = stringResource(R.string.no_tasks_yet_tap_to_add_one),
                icon = Icons.TwoTone.Category,
                onActionClick = { onNavigateToAdd() }
            )
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
                        onItemClick = {
                            task.id?.let { id ->
                                navController.navigate(ZenDoRoutes.PomodoroTask.passId(id))
                            }
                        },
                        onLongItemClick = { onLongItemClick(task) },
                        onPlayClick = { }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskTabletLayout(
    navController: NavController,
    tasks: List<TaskModel>,
    isLoading: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onLongItemClick: (TaskModel) -> Unit,
    onNavigateToAdd: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        ZenDoTopBar(
            title = stringResource(R.string.tasks),
            actionIcon = Icons.Default.Add,
            onActionClick = onNavigateToAdd,
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.widthIn(max = 600.dp)) {
            ZenDoInput(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = stringResource(R.string.search_task),
                leadingIcon = Icons.Default.Search
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (tasks.isEmpty()) {
            ZenDoEmptyState(
                text = stringResource(R.string.no_tasks_yet_tap_to_add_one),
                icon = Icons.TwoTone.Category,
                onActionClick = { onNavigateToAdd() }
            )
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
                        onItemClick = {
                            task.id?.let { id ->
                                navController.navigate(ZenDoRoutes.PomodoroTask.passId(id))
                            }
                        },
                        onLongItemClick = { onLongItemClick(task) },
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
    TaskScreen(
        navController = rememberNavController(),
    )
}

@Preview(name = "Tablet", showBackground = true, device = Devices.PIXEL_C)
@Composable
fun PreviewTaskTabletLayout() {
    TaskScreen(
        navController = rememberNavController(),
    )
}