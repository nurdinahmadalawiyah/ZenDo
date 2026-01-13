package com.dinzio.zendo.features.task.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoButton
import com.dinzio.zendo.core.presentation.components.ZenDoChip
import com.dinzio.zendo.core.presentation.components.ZenDoThumbnailPicker
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.util.TaskConstants
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.features.category.domain.model.CategoryModel
import com.dinzio.zendo.features.category.presentation.component.AddCategoryBottomSheet
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionViewModel
import com.dinzio.zendo.features.task.presentation.component.CategorySelection
import com.dinzio.zendo.features.task.presentation.component.SessionIntervalPicker
import com.dinzio.zendo.features.task.presentation.component.TaskFormFields
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionEvent
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionState
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavHostController,
    viewModel: TaskActionViewModel = hiltViewModel(),
    categoryActionViewModel: CategoryActionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    val isLandscapeMode = isLandscape()

    var showAddCategorySheet by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.popBackStack()
        }
    }

    if (showAddCategorySheet) {
        AddCategoryBottomSheet(
            viewModel = categoryActionViewModel,
            onDismiss = { showAddCategorySheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        )
    }

    if (isLandscapeMode) {
        AddTaskTabletLayout(
            state = state,
            categories = categories,
            onEvent = viewModel::onEvent,
            onAddCategoryClick = { showAddCategorySheet = true }
        )
    } else {
        AddTaskPhoneLayout(
            state = state,
            categories = categories,
            onEvent = viewModel::onEvent,
            onAddCategoryClick = { showAddCategorySheet = true }
        )
    }
}

@Composable
fun AddTaskPhoneLayout(
    state: TaskActionState,
    categories: List<CategoryModel>,
    onEvent: (TaskActionEvent) -> Unit,
    onAddCategoryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                ZenDoTopBar(
                    title = stringResource(R.string.add_task),
                    actionIcon = Icons.Default.MoreVert,
                    isOnPrimaryBackground = true
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp)) {
                ZenDoButton(
                    text = stringResource(R.string.save_task),
                    isLoading = state.isSaving,
                    onClick = { onEvent(TaskActionEvent.OnSaveTask) },
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ZenDoThumbnailPicker(
                currentIcon = state.iconInput,
                onIconSelected = { emoji ->
                    onEvent(TaskActionEvent.OnIconChange(emoji))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            TaskFormFields(state, onEvent)
            Spacer(modifier = Modifier.height(24.dp))
            SessionIntervalPicker(
                currentValue = state.sessionCountInput,
                onValueChange = { onEvent(TaskActionEvent.OnSessionCountChange(it)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            CategorySelection(state, categories, onEvent, onAddCategoryClick)
        }
    }
}

@Composable
fun AddTaskTabletLayout(
    state: TaskActionState,
    categories: List<CategoryModel>,
    onEvent: (TaskActionEvent) -> Unit,
    onAddCategoryClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            ZenDoTopBar(title = stringResource(R.string.add_task), isOnPrimaryBackground = true)
            Spacer(modifier = Modifier.height(32.dp))
            ZenDoThumbnailPicker(
                currentIcon = state.iconInput,
                onIconSelected = { emoji ->
                    onEvent(TaskActionEvent.OnIconChange(emoji))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            CategorySelection(state, categories, onEvent, onAddCategoryClick)
        }

        Column(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .verticalScroll(rememberScrollState())
            ) {
                TaskFormFields(state, onEvent)
                Spacer(modifier = Modifier.height(24.dp))
                SessionIntervalPicker(
                    currentValue = state.sessionCountInput,
                    onValueChange = { onEvent(TaskActionEvent.OnSessionCountChange(it)) }
                )
                Spacer(modifier = Modifier.height(48.dp))
            }

            ZenDoButton(
                text = stringResource(R.string.save_task),
                isLoading = state.isSaving,
                onClick = { onEvent(TaskActionEvent.OnSaveTask) },
                enabled = !state.isSaving
            )
        }
    }
}