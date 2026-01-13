package com.dinzio.zendo.features.task.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoChip
import com.dinzio.zendo.features.category.domain.model.CategoryModel
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionEvent
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionState

@Composable
fun CategorySelection(
    state: TaskActionState,
    categories: List<CategoryModel>,
    onEvent: (TaskActionEvent) -> Unit,
    onAddCategoryClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.category),
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray,
        modifier = Modifier
            .padding(bottom = 8.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ZenDoChip(
            label = "+",
            isSelected = false,
            onClick = onAddCategoryClick
        )

        categories.forEach { category ->
            ZenDoChip(
                label = category.name,
                isSelected = state.categoryIdInput == category.id,
                onClick = {
                    onEvent(TaskActionEvent.OnCategoryChange(category.id))
                }
            )
        }
    }
}