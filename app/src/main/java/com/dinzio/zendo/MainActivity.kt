package com.dinzio.zendo

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.core.navigation.ZenDoNavGraph
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.core.presentation.components.ZenDoBottomBar
import com.dinzio.zendo.core.presentation.components.ZenDoNavigationRail
import com.dinzio.zendo.core.presentation.components.ZenDoSelectionSheet
import com.dinzio.zendo.core.theme.ZendoTheme
import com.dinzio.zendo.features.category.presentation.component.AddCategoryBottomSheet
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionViewModel
import com.dinzio.zendo.features.home.presentation.component.BannerSection
import com.dinzio.zendo.features.task.domain.model.TaskModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val themeMode by mainViewModel.themeMode.collectAsState()
            val langCode by mainViewModel.languageCode.collectAsState()
            val focusTime by mainViewModel.focusTime.collectAsState()
            val breakTime by mainViewModel.breakTime.collectAsState()
            val currentTask by mainViewModel.currentTaskBannerState.collectAsState()

            LaunchedEffect(langCode) {
                val appLocale: LocaleListCompat = if (langCode == "system") {
                    LocaleListCompat.getEmptyLocaleList()
                } else {
                    LocaleListCompat.forLanguageTags(langCode)
                }
                if (AppCompatDelegate.getApplicationLocales() != appLocale) {
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }

            val useDarkTheme = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            ZendoTheme(darkTheme = useDarkTheme) {
                RequestNotificationPermission()
                MainScreen(
                    currentThemeMode = themeMode,
                    onThemeChange = { mainViewModel.setTheme(it) },
                    currentLanguage = langCode,
                    onLanguageChange = { mainViewModel.setLanguage(it) },
                    currentFocusTime = focusTime,
                    onFocusTimeChange = { mainViewModel.setFocusTime(it) },
                    currentBreakTime = breakTime,
                    onBreakTimeChange = { mainViewModel.setBreakTime(it) },
                    currentTask = currentTask,
                )
            }
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            android.widget.Toast.makeText(
                context,
                context.getString(R.string.timer_may_not_be_visible_in_the_background_without_notification_permission),
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentThemeMode: String,
    onThemeChange: (String) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    currentFocusTime: Int,
    onFocusTimeChange: (Int) -> Unit,
    currentBreakTime: Int,
    onBreakTimeChange: (Int) -> Unit,
    currentTask: TaskModel?,
    actionViewModel: CategoryActionViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ZenDoRoutes.Home.route

    val isFullScreenRoute = currentRoute.contains(ZenDoRoutes.DetailCategory.route)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val showBottomNav = currentRoute in listOf(
        ZenDoRoutes.Home.route,
        ZenDoRoutes.Focus.route,
        ZenDoRoutes.Stats.route,
        ZenDoRoutes.Settings.route
    )

    var showLandscapeBanner by remember { mutableStateOf(false) }

    var showSelectionSheet by remember { mutableStateOf(false) }
    val selectionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showAddCategorySheet by remember { mutableStateOf(false) }
    val addCategorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showSelectionSheet) {
        ZenDoSelectionSheet(
            onDismiss = { showSelectionSheet = false },
            onAddTask = { navController.navigate(ZenDoRoutes.AddTask.route) },
            onAddCategory = { showAddCategorySheet = true },
            sheetState = selectionSheetState
        )
    }

    if (showAddCategorySheet) {
        AddCategoryBottomSheet(
            viewModel = actionViewModel,
            onDismiss = { showAddCategorySheet = false },
            sheetState = addCategorySheetState
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = if (isFullScreenRoute) Color.Transparent else MaterialTheme.colorScheme.background,
            contentWindowInsets = if (isFullScreenRoute) WindowInsets(0, 0, 0, 0) else ScaffoldDefaults.contentWindowInsets,
            bottomBar = {
                if (!isLandscape && showBottomNav) {
                    Column {
                        BannerSection(
                            navController = navController,
                            currentTask = currentTask,
                            roundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        ZenDoBottomBar(
                            currentRoute = currentRoute,
                            onPlusClick = { showSelectionSheet = true },
                            onNavigate = { route -> navController.navigate(route) }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    if (isLandscape && showBottomNav) {
                        ZenDoNavigationRail(
                            currentRoute = currentRoute,
                            onPlusClick = { showSelectionSheet = true },
                            onNavigate = { route -> navController.navigate(route) },
                            currentTask = currentTask,
                            onTaskIconClick = { showLandscapeBanner = !showLandscapeBanner }
                        )
                    }
                    ZenDoNavGraph(
                        navController = navController,
                        currentTheme = currentThemeMode,
                        onThemeChange = onThemeChange,
                        currentLanguage = currentLanguage,
                        onLanguageChange = onLanguageChange,
                        currentFocusTime = currentFocusTime,
                        onFocusTimeChange = onFocusTimeChange,
                        currentBreakTime = currentBreakTime,
                        onBreakTimeChange = onBreakTimeChange
                    )
                }

                if (isLandscape && showBottomNav && currentTask != null && showLandscapeBanner) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 80.dp, bottom = 16.dp)
                            .fillMaxWidth(0.5f)
                    ) {
                        BannerSection(
                            navController = navController,
                            currentTask = currentTask,
                            roundedCornerShape = RoundedCornerShape(24.dp)
                        )
                    }
                }
            }
        }
    }
}