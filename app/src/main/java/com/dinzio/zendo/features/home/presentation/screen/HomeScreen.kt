package com.dinzio.zendo.features.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.R
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.core.presentation.components.ZenDoSectionHeader
import com.dinzio.zendo.core.presentation.components.ZenDoTaskItemCard
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionState
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionViewModel
import com.dinzio.zendo.features.category.presentation.viewModel.categoryList.CategoryListState
import com.dinzio.zendo.features.category.presentation.viewModel.categoryList.CategoryListViewModel
import com.dinzio.zendo.features.home.presentation.component.BannerSection
import com.dinzio.zendo.features.home.presentation.component.CategorySection
import com.dinzio.zendo.features.home.presentation.component.HeaderSection
import com.dinzio.zendo.features.home.presentation.component.TaskSection
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionState
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionViewModel
import com.dinzio.zendo.features.task.presentation.viewModel.taskList.TaskListState
import com.dinzio.zendo.features.task.presentation.viewModel.taskList.TaskListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    categoryListViewModel: CategoryListViewModel = hiltViewModel(),
    categoryActionViewModel: CategoryActionViewModel = hiltViewModel(),
    taskListViewModel: TaskListViewModel = hiltViewModel(),
    taskActionViewModel: TaskActionViewModel = hiltViewModel(),
) {
    val isLandscapeMode = isLandscape()

    val categoryListState by categoryListViewModel.state.collectAsStateWithLifecycle()
    val categoryActionState by categoryActionViewModel.state.collectAsStateWithLifecycle()

    val taskListState by taskListViewModel.state.collectAsStateWithLifecycle()
    val taskActionState by taskActionViewModel.state.collectAsStateWithLifecycle()

    if (isLandscapeMode) {
        HomeTabletLayout(
            navController = navController,
            categoryListViewModel = categoryListViewModel,
            categoryActionViewModel = categoryActionViewModel,
            categoryListState = categoryListState,
            categoryActionState = categoryActionState,
            taskListViewModel = taskListViewModel,
            taskActionViewModel = taskActionViewModel,
            taskListState = taskListState,
            taskActionState = taskActionState,
        )
    } else {
        HomePhoneLayout(
            navController = navController,
            categoryListViewModel = categoryListViewModel,
            categoryActionViewModel = categoryActionViewModel,
            categoryListState = categoryListState,
            categoryActionState = categoryActionState,
            taskListViewModel = taskListViewModel,
            taskActionViewModel = taskActionViewModel,
            taskListState = taskListState,
            taskActionState = taskActionState,
        )
    }
}

@Composable
fun HomePhoneLayout(
    navController: NavController,
    categoryListViewModel: CategoryListViewModel,
    categoryActionViewModel: CategoryActionViewModel,
    categoryListState: CategoryListState,
    categoryActionState: CategoryActionState,
    taskListViewModel: TaskListViewModel,
    taskActionViewModel: TaskActionViewModel,
    taskListState: TaskListState,
    taskActionState: TaskActionState,
) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(PaddingValues(16.dp))) {
                HeaderSection(navController)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            item {
                BannerSection()
                Spacer(modifier = Modifier.height(24.dp))

                CategorySection(
                    navController = navController,
                    categoryListViewModel = categoryListViewModel,
                    categoryActionViewModel = categoryActionViewModel,
                    categoryListState = categoryListState,
                    categoryActionState = categoryActionState,
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                TaskSection(
                    navController = navController,
                    taskListViewModel = taskListViewModel,
                    taskActionViewModel = taskActionViewModel,
                    taskListState = taskListState,
                    taskActionState = taskActionState,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun HomeTabletLayout(
    navController: NavController,
    categoryListViewModel: CategoryListViewModel,
    categoryActionViewModel: CategoryActionViewModel,
    categoryListState: CategoryListState,
    categoryActionState: CategoryActionState,
    taskListViewModel: TaskListViewModel,
    taskActionViewModel: TaskActionViewModel,
    taskListState: TaskListState,
    taskActionState: TaskActionState,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(navController)

            Spacer(modifier = Modifier.height(24.dp))

            BannerSection()
            Spacer(modifier = Modifier.height(24.dp))

            CategorySection(
                navController = navController,
                categoryListViewModel = categoryListViewModel,
                categoryActionViewModel = categoryActionViewModel,
                categoryListState = categoryListState,
                categoryActionState = categoryActionState,
            )

        }

        TaskSection(
            navController = navController,
            taskListViewModel = taskListViewModel,
            taskActionViewModel = taskActionViewModel,
            taskListState = taskListState,
            taskActionState = taskActionState,
            modifier = Modifier
                .weight(0.55f)
                .verticalScroll(rememberScrollState())
                .fillMaxHeight(),
        )
    }
}

@Preview(name = "Portrait Phone", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewPortrait() {
    HomeScreen(
        navController = rememberNavController()
    )
}

@Preview(
    name = "Landscape Mode",
    showBackground = true,
    device = "spec:width=800dp,height=411dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun PreviewLandscape() {
    HomeScreen(
        navController = rememberNavController()
    )
}