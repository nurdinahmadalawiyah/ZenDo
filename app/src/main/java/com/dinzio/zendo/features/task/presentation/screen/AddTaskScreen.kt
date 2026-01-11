package com.dinzio.zendo.features.task.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dinzio.zendo.core.presentation.components.ZenDoButton
import com.dinzio.zendo.core.presentation.components.ZenDoChip
import com.dinzio.zendo.core.presentation.components.ZenDoDropDown
import com.dinzio.zendo.core.presentation.components.ZenDoInput
import com.dinzio.zendo.core.presentation.components.ZenDoThumbnailPicker
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.features.task.presentation.component.SessionIntervalPicker
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionEvent
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionState
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionViewModel

@Composable
fun AddTaskScreen(
    navController: NavHostController,
    viewModel: TaskActionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLandscapeMode = isLandscape()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.popBackStack()
        }
    }

    if (isLandscapeMode) {
        AddTaskTabletLayout(
            state = state,
            onEvent = viewModel::onEvent
        )
    } else {
        AddTaskPhoneLayout(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun AddTaskPhoneLayout(
    state: TaskActionState,
    onEvent: (TaskActionEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ZenDoTopBar(
            title = "Add Task",
            actionIcon = Icons.Default.MoreVert,
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(24.dp))
        ZenDoThumbnailPicker(
            currentIcon = state.iconInput,
            onIconSelected = { emoji ->
                onEvent(TaskActionEvent.OnIconChange(emoji))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Title",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )
        ZenDoInput(
            value = state.titleInput,
            onValueChange = { onEvent(TaskActionEvent.OnTitleChange(it)) },
            placeholder = "Task Title"
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ZenDoDropDown(
                label = "Focus",
                value = "25 minutes", // Statis dulu sementara
                onClick = { },
                modifier = Modifier.weight(1f)
            )
            ZenDoDropDown(
                label = "Break",
                value = "5 minutes", // Statis dulu sementara
                onClick = { },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        SessionIntervalPicker(
            currentValue = state.sessionCountInput,
            onValueChange = { onEvent(TaskActionEvent.OnSessionCountChange(it)) }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Category",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ZenDoChip(
                label = "None",
                isSelected = state.categoryIdInput == null,
                onClick = { onEvent(TaskActionEvent.OnCategoryChange(null)) }
            )
            ZenDoChip(
                label = "Work",
                isSelected = state.categoryIdInput == 1,
                onClick = { onEvent(TaskActionEvent.OnCategoryChange(1)) }
            )
            ZenDoChip(
                label = "Hobbies",
                isSelected = state.categoryIdInput == 2,
                onClick = { onEvent(TaskActionEvent.OnCategoryChange(2)) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        ZenDoButton(
            text = if (state.isSaving) "Saving..." else "Save Task",
            onClick = { onEvent(TaskActionEvent.OnSaveTask) },
            enabled = !state.isSaving
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AddTaskTabletLayout(
    state: TaskActionState,
    onEvent: (TaskActionEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            ZenDoTopBar(title = "Add Task", isOnPrimaryBackground = true)
            Spacer(modifier = Modifier.height(32.dp))
            ZenDoThumbnailPicker(
                currentIcon = state.iconInput,
                onIconSelected = { emoji ->
                    onEvent(TaskActionEvent.OnIconChange(emoji))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Category",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ZenDoChip(
                    label = "None",
                    isSelected = state.categoryIdInput == null,
                    onClick = { onEvent(TaskActionEvent.OnCategoryChange(null)) }
                )
                ZenDoChip(
                    label = "Work",
                    isSelected = state.categoryIdInput == 1,
                    onClick = { onEvent(TaskActionEvent.OnCategoryChange(1)) }
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1.2f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Title",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )
            ZenDoInput(
                value = state.titleInput,
                onValueChange = { onEvent(TaskActionEvent.OnTitleChange(it)) },
                placeholder = "Task Title"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ZenDoDropDown(
                    label = "Focus",
                    value = "25 minutes",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                ZenDoDropDown(
                    label = "Break",
                    value = "5 minutes",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            SessionIntervalPicker(
                currentValue = state.sessionCountInput,
                onValueChange = { onEvent(TaskActionEvent.OnSessionCountChange(it) ) }
            )
            Spacer(modifier = Modifier.height(48.dp))
            ZenDoButton(
                text = if (state.isSaving) "Saving..." else "Save Task",
                onClick = { onEvent(TaskActionEvent.OnSaveTask) },
                enabled = !state.isSaving
            )
        }
    }
}