package com.dinzio.zendo.features.task.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoDropDown
import com.dinzio.zendo.core.presentation.components.ZenDoInput
import com.dinzio.zendo.core.util.TaskConstants
import com.dinzio.zendo.core.util.extractMinutes
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionEvent
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionState

@Composable
fun TaskFormFields(
    state: TaskActionState,
    onEvent: (TaskActionEvent) -> Unit,
) {
    val focusOptions = TaskConstants.FOCUS_OPTIONS_RES.map { stringResource(it) }
    val breakOptions = TaskConstants.BREAK_OPTIONS_RES.map { stringResource(it) }

    Text(
        text = stringResource(R.string.title),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .padding(bottom = 8.dp)
    )
    ZenDoInput(
        value = state.titleInput,
        onValueChange = { onEvent(TaskActionEvent.OnTitleChange(it)) },
        placeholder = stringResource(R.string.task_title)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ZenDoDropDown(
            label = stringResource(R.string.focus_time),
            selectedValue = focusOptions.find { it.extractMinutes() == state.focusTimeInput } ?: focusOptions[1],
            options = focusOptions,
            onOptionSelected = {
                onEvent(TaskActionEvent.OnFocusTimeChange(it.extractMinutes()))
            },
            modifier = Modifier.weight(1f)
        )
        ZenDoDropDown(
            label = stringResource(R.string.break_time),
            selectedValue = breakOptions.find { it.extractMinutes() == state.breakTimeInput } ?: breakOptions[0],
            options = breakOptions,
            onOptionSelected = {
                onEvent(TaskActionEvent.OnBreakTimeChange(it.extractMinutes()))
            },
            modifier = Modifier.weight(1f)
        )
    }
}