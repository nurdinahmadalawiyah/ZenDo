package com.dinzio.zendo.features.category.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoInput
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionEvent
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionState

@Composable
fun CategoryFormFields(
    state: CategoryActionState,
    onEvent: (CategoryActionEvent) -> Unit,
) {
    Text(
        text = stringResource(R.string.category_name),
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray,
        modifier = Modifier
            .padding(bottom = 8.dp)
    )
    ZenDoInput(
        value = state.nameInput,
        onValueChange = { onEvent(CategoryActionEvent.OnNameChange(it)) },
        placeholder = stringResource(R.string.category_name)
    )
}