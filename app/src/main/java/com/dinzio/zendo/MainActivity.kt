package com.dinzio.zendo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.core.theme.ZendoTheme
import com.dinzio.zendo.core.presentation.components.ZenDoBottomBar
import com.dinzio.zendo.core.presentation.components.ZenDoNavigationRail
import com.dinzio.zendo.core.navigation.ZenDoNavGraph
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.core.presentation.components.ZenDoSelectionSheet
import com.dinzio.zendo.features.category.presentation.component.AddCategoryBottomSheet
import com.dinzio.zendo.features.category.presentation.viewModel.categoryAction.CategoryActionViewModel
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
                MainScreen(
                    currentThemeMode = themeMode,
                    currentLanguage = langCode,
                    onThemeChange = { mainViewModel.setTheme(it) },
                    onLanguageChange = { mainViewModel.setLanguage(it) }
                )
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
    actionViewModel: CategoryActionViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ZenDoRoutes.Home.route

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val showBottomNav = currentRoute in listOf(ZenDoRoutes.Home.route, ZenDoRoutes.Focus.route, ZenDoRoutes.Stats.route, ZenDoRoutes.Settings.route)

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
            bottomBar = {
                if (!isLandscape && showBottomNav) {
                    ZenDoBottomBar(
                        currentRoute = currentRoute,
                        onPlusClick = { showSelectionSheet = true },
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
            }
        ) { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (isLandscape && showBottomNav) {
                    ZenDoNavigationRail(
                        currentRoute = currentRoute,
                        onPlusClick = { showSelectionSheet = true },
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
                ZenDoNavGraph(
                    navController = navController,
                    currentTheme = currentThemeMode,
                    onThemeChange = onThemeChange,
                    currentLanguage = currentLanguage,
                    onLanguageChange = onLanguageChange
                )
            }
        }
    }
}