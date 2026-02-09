package com.dinzio.zendo.features.category.presentation.screen

import ZenDoEmptyState
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Task
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.R
import com.dinzio.zendo.core.theme.White
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.core.presentation.components.ZenDoSectionHeader
import com.dinzio.zendo.core.presentation.components.ZenDoTaskItemCard
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.core.presentation.components.ZenDoActionSheet
import com.dinzio.zendo.core.presentation.components.ZenDoConfirmDialog
import com.dinzio.zendo.features.category.presentation.viewModel.categoryDetail.CategoryDetailState
import com.dinzio.zendo.features.category.presentation.viewModel.categoryDetail.CategoryDetailViewModel
import com.dinzio.zendo.features.home.presentation.screen.TaskUiModel
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionEvent
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionState
import com.dinzio.zendo.features.task.presentation.viewModel.taskAction.TaskActionViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailCategoryScreen(
    navController: NavController,
    viewModel: CategoryDetailViewModel = hiltViewModel(),
    taskActionViewModel: TaskActionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val taskActionState by taskActionViewModel.state.collectAsStateWithLifecycle()

    val isLandscapeMode = isLandscape()

    if (isLandscapeMode) {
        DetailCategoryTabletLayout(
            navController = navController,
            state = state,
            taskActionViewModel = taskActionViewModel,
            taskActionState = taskActionState,
        )
    } else {
        DetailCategoryPhoneLayout(
            navController = navController,
            state = state,
            taskActionViewModel = taskActionViewModel,
            taskActionState = taskActionState,
        )
    }
}

@Composable
fun DetailCategoryPhoneLayout(
    navController: NavController,
    state: CategoryDetailState,
    taskActionViewModel: TaskActionViewModel,
    taskActionState: TaskActionState,
) {
    val view = LocalView.current
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme() // Cek apakah HP sedang mode gelap

    // Gunakan DisposableEffect agar saat navigasi BACK, status bar kembali ke settingan awal (Theme.kt)
    DisposableEffect(key1 = isDarkTheme) {
        val window = (context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)

        // SAAT MASUK KE HALAMAN INI:
        // Karena background atas hijau (Gelap), kita paksa icon jadi putih (isAppearanceLightStatusBars = false)
        insetsController.isAppearanceLightStatusBars = false

        onDispose {
            // SAAT KELUAR DARI HALAMAN INI:
            // Kembalikan ke settingan asli aplikasi (jika gelap = putih, jika terang = hitam)
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            ZenDoTopBar(
                title = state.categoryDetail?.category?.name ?: "Title",
                actionIcon = Icons.Default.Add,
                onActionClick = { navController.navigate(ZenDoRoutes.AddTask.route) },
                isOnPrimaryBackground = false
            )
        }

        Box(modifier = Modifier.padding(16.dp)) {
            CategoryInfoCard(state = state)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            TaskList(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                state = state,
                taskActionViewModel = taskActionViewModel,
                taskActionState = taskActionState,
            )
        }
    }
}

@Composable
fun DetailCategoryTabletLayout(
    navController: NavController,
    state: CategoryDetailState,
    taskActionViewModel: TaskActionViewModel,
    taskActionState: TaskActionState,
) {
    Row(
        modifier = Modifier.fillMaxSize().statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .padding(16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp)
        ) {
            ZenDoTopBar(
                title = state.categoryDetail?.category?.name ?: "Title",
                actionIcon = Icons.Default.Add,
                onActionClick = { navController.navigate(ZenDoRoutes.AddTask.route) },
                isOnPrimaryBackground = false
            )

            Spacer(modifier = Modifier.height(32.dp))

            CategoryInfoCard(
                state = state,
                tabletMode = true
            )
        }

        Column(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            ZenDoSectionHeader(title = stringResource(id = R.string.task_list), showActionText = false, onActionClick = {})

            Spacer(modifier = Modifier.height(16.dp))

            TaskList(
                navController = navController,
                state = state,
                taskActionViewModel = taskActionViewModel,
                taskActionState = taskActionState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskList(
    navController: NavController,
    modifier: Modifier = Modifier,
    state: CategoryDetailState,
    taskActionViewModel: TaskActionViewModel,
    taskActionState: TaskActionState,
) {
    var selectedTask by remember { mutableStateOf<TaskModel?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showActionSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(taskActionState.isSuccess) {
        if (taskActionState.isSuccess) {
            showActionSheet = false
            showDeleteDialog = false
            selectedTask = null
            taskActionViewModel.onEvent(TaskActionEvent.OnResetTask)
        }
    }

    if (showActionSheet && selectedTask != null) {
        ZenDoActionSheet(
            title = selectedTask?.title ?: "",
            icon = selectedTask?.icon ?: "",
            sheetState = sheetState,
            onDismiss = { showActionSheet = false },
            onEditClick = {
                showActionSheet = false
                navController.navigate(ZenDoRoutes.EditTask.passId(selectedTask!!.id))
            },
            onDeleteClick = { showDeleteDialog = true }
        )
    }

    if (showDeleteDialog && selectedTask != null) {
        selectedTask?.title?.let {
            ZenDoConfirmDialog(
                title = stringResource(R.string.delete_task),
                message = stringResource(
                    R.string.are_you_sure_you_want_to_delete_this_action_cannot_be_undone,
                    it
                ),
                confirmText = stringResource(R.string.delete),
                dismissText = stringResource(R.string.cancel),
                onConfirm = {
                    taskActionViewModel.onEvent(TaskActionEvent.OnDeleteTask(selectedTask!!))
                },
                onDismiss = {
                    showDeleteDialog = false
                },
                isLoading = taskActionState.isDeleting
            )
        }
    }

    val tabs = listOf(stringResource(R.string.pending), stringResource(R.string.completed))

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            divider = {},
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                    width = 60.dp,
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            val currentTasks = if (pageIndex == 0) state.pendingTasks else state.completedTasks

            if (currentTasks.isEmpty()) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ZenDoEmptyState(
                        text = stringResource(R.string.no_tasks_yet, tabs[pageIndex].lowercase()),
                        icon = Icons.TwoTone.Task,
                        onActionClick = { navController.navigate(ZenDoRoutes.AddTask.route) }
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(currentTasks) { task ->
                        ZenDoTaskItemCard(
                            title = task.title,
                            sessionCount = task.sessionCount.toString(),
                            sessionDone = task.sessionDone.toString(),
                            categoryIcon = task.icon,
                            onItemClick = {
                                task.id?.let { id ->
                                    navController.navigate(ZenDoRoutes.PomodoroTask.passId(id))
                                }
                            },
                            onLongItemClick = {
                                selectedTask = task
                                showActionSheet = true
                            },
                            onPlayClick = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryInfoCard(
    state: CategoryDetailState,
    tabletMode: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (tabletMode) {
                    Modifier.fillMaxHeight()
                } else {
                    Modifier.height(140.dp)
                }
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = if (tabletMode) Arrangement.Center else Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = if (tabletMode) Modifier.fillMaxWidth() else Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = if (tabletMode) Alignment.CenterHorizontally else Alignment.Start
            ) {
                Text(
                    text = state.categoryDetail?.category?.name ?: "Title",
                    style = if (tabletMode) MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ) else MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        id = R.string.task,
                        state.categoryDetail?.totalTasks ?: 0
                    ),
                    style = if (tabletMode) MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium) else MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = White
                )

                if (tabletMode) {
                    Spacer(modifier = Modifier.height(64.dp))
                    Text(
                        text = state.categoryDetail?.category?.icon ?: "\uD83D\uDCDA\uFE0F",
                        fontSize = 200.sp,
                    )
                }
            }

            if (!tabletMode) {
                Text(
                    text = state.categoryDetail?.category?.icon ?: "\uD83D\uDCDA\uFE0F",
                    fontSize = 64.sp,
                )
            }
        }
    }
}

val dummyDetailTasks = listOf(
    TaskUiModel("Learn Angular", "2 Sessions", "4 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Create Report", "1 Sessions", "1 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Fix Bugs", "3 Sessions", "2 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Gym", "4 Sessions", " 1 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel(
        "Reading",
        "1 Sessions",
        " 0 Done",
        "\uD83D\uDCD6"
    ),
    TaskUiModel("Meditation", "1 Sessions", " 1 Done", "\uD83E\uDDD8\u200D\u2642\uFE0F")
)

@Preview(name = "Phone Portrait", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewDetailPhone() {
    DetailCategoryScreen(
        navController = rememberNavController(),
    )
}

@Preview(
    name = "Tablet Landscape",
    showBackground = true,
    device = "spec:width=800dp,height=1280dp,dpi=320,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun PreviewDetailTablet() {
    DetailCategoryScreen(
        navController = rememberNavController(),
    )
}