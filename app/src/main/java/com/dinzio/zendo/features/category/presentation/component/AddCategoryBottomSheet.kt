package com.dinzio.zendo.features.category.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.core.presentation.components.ZenDoButton
import com.dinzio.zendo.core.presentation.components.ZenDoInput
import com.dinzio.zendo.core.presentation.components.ZenDoThumbnailPicker
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionEvent
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryBottomSheet(
    viewModel: CategoryActionViewModel,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onDismiss()
            viewModel.onEvent(CategoryActionEvent.OnResetSuccess)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 40.dp),
        ) {
            Text(
                text = "Add Category",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            ZenDoThumbnailPicker(
                currentIcon = state.iconInput,
                onIconSelected = { emoji ->
                    viewModel.onEvent(CategoryActionEvent.OnIconChange(emoji))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Category Name",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )
            ZenDoInput(
                value = state.nameInput,
                onValueChange = { viewModel.onEvent(CategoryActionEvent.OnNameChange(it)) },
                placeholder = "Category Name"
            )

            Spacer(modifier = Modifier.height(32.dp))
            ZenDoButton(
                text = if (state.isSaving) "Saving..." else "Save Category",
                onClick = { viewModel.onEvent(CategoryActionEvent.OnSaveCategory) },
                enabled = !state.isSaving
            )
        }
    }
}