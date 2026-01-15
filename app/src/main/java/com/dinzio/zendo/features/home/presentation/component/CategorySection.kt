package com.dinzio.zendo.features.home.presentation.component

import ZenDoEmptyState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.dinzio.zendo.core.presentation.components.ZenDoCategoryCard
import com.dinzio.zendo.core.presentation.components.ZenDoConfirmDialog
import com.dinzio.zendo.core.presentation.components.ZenDoSectionHeader
import com.dinzio.zendo.features.category.domain.model.CategoryModel
import com.dinzio.zendo.features.category.presentation.component.AddCategoryBottomSheet
import com.dinzio.zendo.features.category.presentation.component.EditCategoryBottomSheet
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionEvent
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionState
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionViewModel
import com.dinzio.zendo.features.category.presentation.viewModel.categoryList.CategoryListState
import com.dinzio.zendo.features.category.presentation.viewModel.categoryList.CategoryListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySection(
    navController: NavController,
    categoryListViewModel: CategoryListViewModel,
    categoryActionViewModel: CategoryActionViewModel,
    categoryListState: CategoryListState,
    categoryActionState: CategoryActionState,
) {

    var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showActionSheet by remember { mutableStateOf(false) }
    var showAddCategorySheet by remember { mutableStateOf(false) }
    var showEditCategorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(categoryActionState.isSuccess) {
        if (categoryActionState.isSuccess) {
            showActionSheet = false
            showDeleteDialog = false
            selectedCategory = null
            categoryActionViewModel.onEvent(CategoryActionEvent.OnResetSuccess)
        }
    }

    if (showActionSheet && selectedCategory != null) {
        ZenDoActionSheet(
            title = selectedCategory?.name ?: "",
            icon = selectedCategory?.icon ?: "",
            sheetState = sheetState,
            onDismiss = { showActionSheet = false },
            onEditClick = {
                selectedCategory?.let {
                    categoryActionViewModel.onEvent(CategoryActionEvent.OnLoadCategory(it.id))
                    showEditCategorySheet = true
                }
                showActionSheet = false
            },
            onDeleteClick = { showDeleteDialog = true }
        )
    }

    if (showDeleteDialog && selectedCategory != null) {
        selectedCategory?.name?.let {
            ZenDoConfirmDialog(
                title = stringResource(R.string.delete_category),
                message = stringResource(
                    R.string.are_you_sure_you_want_to_delete_this_action_cannot_be_undone,
                    it
                ),
                confirmText = stringResource(R.string.delete),
                dismissText = stringResource(R.string.cancel),
                onConfirm = {
                    categoryActionViewModel.onEvent(CategoryActionEvent.OnDeleteCategory(selectedCategory!!))
                },
                onDismiss = {
                    showDeleteDialog = false
                },
                isLoading = categoryActionState.isDeleting
            )
        }
    }

    if (showAddCategorySheet) {
        AddCategoryBottomSheet(
            viewModel = categoryActionViewModel,
            onDismiss = { showAddCategorySheet = false },
            sheetState = sheetState
        )
    }

    if (showEditCategorySheet) {
        EditCategoryBottomSheet(
            viewModel = categoryActionViewModel,
            onDismiss = { showEditCategorySheet = false },
            sheetState = sheetState
        )
    }

    Column {
        ZenDoSectionHeader(
            title = stringResource(R.string.categories),
            onActionClick = { navController.navigate(ZenDoRoutes.Categories.route) })
        Spacer(modifier = Modifier.height(12.dp))

        if (categoryListState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (categoryListState.categories.isEmpty()) {
            ZenDoEmptyState(
                text = stringResource(R.string.no_categories_yet_tap_to_add_one),
                icon = Icons.TwoTone.Category,
                onActionClick = { showAddCategorySheet = true }
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categoryListState.categories.take(4)) { category ->
                    ZenDoCategoryCard(
                        title = category.name,
                        taskCount = category.taskCount,
                        icon = category.icon,
                        onLongItemClick = {
                            selectedCategory = category
                            showActionSheet = true
                        },
                        onClick = { navController.navigate(ZenDoRoutes.DetailCategory.passId(category.id)) }
                    )
                }
            }
        }
    }
}