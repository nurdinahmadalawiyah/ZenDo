package com.dinzio.zendo.features.category.presentation.screen

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.dinzio.zendo.R
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.core.presentation.components.ZenDoCategoryCard
import com.dinzio.zendo.core.presentation.components.ZenDoInput
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.features.category.domain.model.CategoryModel
import com.dinzio.zendo.features.category.presentation.component.AddCategoryBottomSheet
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionViewModel
import com.dinzio.zendo.features.category.presentation.viewModel.categoryList.CategoryListViewModel
import com.dinzio.zendo.features.home.presentation.screen.CategoryUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryListViewModel = hiltViewModel(),
    actionViewModel: CategoryActionViewModel = hiltViewModel(),
) {
    val isLandscapeMode = isLandscape()

    val state by viewModel.state.collectAsStateWithLifecycle()

    var showAddCategorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var searchQuery by remember { mutableStateOf("") }

    val filteredCategory = state.categories.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    if (showAddCategorySheet) {
        AddCategoryBottomSheet(
            viewModel = actionViewModel,
            onDismiss = { showAddCategorySheet = false },
            sheetState = sheetState
        )
    }

    if (isLandscapeMode) {
        CategoryTabletLayout(
            categories = filteredCategory,
            isLoading = state.isLoading,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onAddCategoryClick = { showAddCategorySheet = true }
        )
    } else {
        CategoryPhoneLayout(
            categories = filteredCategory,
            isLoading = state.isLoading,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onAddCategoryClick = { showAddCategorySheet = true }
        )
    }
}

@Composable
fun CategoryPhoneLayout(
    categories: List<CategoryModel>,
    isLoading: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddCategoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ZenDoTopBar(
            title = stringResource(R.string.categories),
            actionIcon = Icons.Default.Add,
            onActionClick = onAddCategoryClick,
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        ZenDoInput(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = stringResource(R.string.search_category),
            leadingIcon = Icons.Default.Search
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(categories) { category ->
                    ZenDoCategoryCard(
                        title = category.name,
                        taskCount = category.taskCount,
                        icon = category.icon,
                        onClick = { /* Navigate to Category Detail */ }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryTabletLayout(
    categories: List<CategoryModel>,
    isLoading: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddCategoryClick: () -> Unit
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
            title = stringResource(R.string.categories),
            actionIcon = Icons.Default.Add,
            onActionClick = onAddCategoryClick,
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.widthIn(max = 600.dp)) {
            ZenDoInput(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = stringResource(R.string.search_category),
                leadingIcon = Icons.Default.Search
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(categories) { category ->
                    ZenDoCategoryCard(
                        title = category.name,
                        taskCount = category.taskCount,
                        icon = category.icon,
                        onClick = { /* Navigate to Category Detail */ }
                    )
                }
            }
        }
    }
}

val dummyCategoriesList = listOf(
    CategoryUiModel("Deep Work", 8, "🧠"),      // Fokus tinggi, tanpa gangguan
    CategoryUiModel("Study", 12, "📚"),         // Belajar umum
    CategoryUiModel("Coding", 15, "💻"),        // Programming
    CategoryUiModel("Writing", 5, "✍️"),        // Menulis artikel/skripsi
    CategoryUiModel("Design", 6, "🎨"),         // UI/UX, Gambar
    CategoryUiModel("Languages", 4, "🗣️"),      // Belajar bahasa asing
    CategoryUiModel("Reading", 7, "📖"),        // Membaca buku non-fiksi
    CategoryUiModel("Email/Admin", 3, "📧"),    // Balas chat/email, rekap
    CategoryUiModel("Planning", 2, "🗓️"),       // Review hari/minggu
    CategoryUiModel("Meditation", 1, "🧘"),     // Mindfulness session
    CategoryUiModel("Chores", 4, "🧹"),         // Bersih-bersih (banyak yg pakai timer!)
    CategoryUiModel("Exercise", 2, "💪"),       // Workout rumahan
)


@Preview(name = "Phone", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewCategoryPhone() {
    CategoryScreen()
}

@Preview(name = "Tablet", showBackground = true, device = Devices.PIXEL_C)
@Composable
fun PreviewCategoryTablet() {
    CategoryScreen()
}