package com.dinzio.zendo.features.category.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoButton
import com.dinzio.zendo.core.presentation.components.ZenDoThumbnailPicker
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionEvent
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryBottomSheet(
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
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 40.dp),
        ) {
            Text(
                text = stringResource(R.string.edit_category),
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

            CategoryFormFields(
                state = state,
                onEvent = viewModel::onEvent
            )

            Spacer(modifier = Modifier.height(32.dp))

            ZenDoButton(
                text = stringResource(R.string.update_category),
                isLoading = state.isSaving,
                onClick = { viewModel.onEvent(CategoryActionEvent.OnSaveCategory) },
                enabled = !state.isSaving
            )
        }
    }
}