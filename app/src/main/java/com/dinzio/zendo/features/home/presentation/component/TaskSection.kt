package com.dinzio.zendo.features.home.presentation.component

import ZenDoEmptyState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dinzio.zendo.R
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.core.presentation.components.ZenDoActionSheet
import com.dinzio.zendo.core.presentation.components.ZenDoConfirmDialog
import com.dinzio.zendo.core.presentation.components.ZenDoLockedTaskDialog
import com.dinzio.zendo.core.presentation.components.ZenDoSectionHeader
import com.dinzio.zendo.core.presentation.components.ZenDoTaskItemCard
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionEvent
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionState
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionViewModel
import com.dinzio.zendo.features.task.presentation.viewModel.taskList.TaskListState
import com.dinzio.zendo.features.task.presentation.viewModel.taskList.TaskListViewModel
import com.dinzio.zendo.features.task.util.TaskInteractionHandler.navigateToTaskSafely


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskSection(
    navController: NavController,
    taskListViewModel: TaskListViewModel,
    taskActionViewModel: TaskActionViewModel,
    taskListState: TaskListState,
    taskActionState: TaskActionState,
    modifier: Modifier,
) {
    var selectedTask by remember { mutableStateOf<TaskModel?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showActionSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showLockedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(taskActionState.isSuccess) {
        if (taskActionState.isSuccess) {
            showActionSheet = false
            showDeleteDialog = false
            selectedTask = null
            taskActionViewModel.onEvent(TaskActionEvent.OnResetTask)
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
                    taskActionViewModel.onEvent(TaskActionEvent.OnDeleteTask(selectedTask!!))
                },
                onDismiss = {
                    showDeleteDialog = false
                },
                isLoading = taskActionState.isDeleting
            )
        }
    }

    if (showLockedDialog) {
        ZenDoLockedTaskDialog(
            onDismiss = { showLockedDialog = false }
        )
    }

    Column(
        modifier = modifier
    ) {
        ZenDoSectionHeader(
            title = stringResource(R.string.task_list),
            onActionClick = { navController.navigate(ZenDoRoutes.Tasks.route) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (taskListState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (taskListState.tasks.isEmpty()) {
            ZenDoEmptyState(
                text = stringResource(R.string.no_tasks_yet_tap_to_add_one),
                icon = Icons.TwoTone.Category,
                onActionClick = { navController.navigate(ZenDoRoutes.AddTask.route) }
            )
        } else {
            Column (
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                taskListState.tasks.take(10).forEach { task ->
                    ZenDoTaskItemCard(
                        title = task.title,
                        sessionCount = task.sessionCount.toString(),
                        sessionDone = task.sessionDone.toString(),
                        categoryIcon = task.icon,
                        onItemClick = {
                            navController.navigateToTaskSafely(task) {
                                showLockedDialog = true
                            }
                        },
                        onLongItemClick = {
                            selectedTask = task
                            showActionSheet = true
                        },
                        onPlayClick = { }
                    )
                }
            }
        }
    }
}