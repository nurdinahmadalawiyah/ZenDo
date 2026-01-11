package com.dinzio.zendo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.core.theme.ZendoTheme
import com.dinzio.zendo.core.presentation.components.ZenDoBottomBar
import com.dinzio.zendo.core.presentation.components.ZenDoNavigationRail
import com.dinzio.zendo.core.navigation.ZenDoNavGraph
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val themeMode by mainViewModel.themeMode.collectAsState()
            val useDarkTheme = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            ZendoTheme(darkTheme = useDarkTheme) {
                MainScreen(
                    currentThemeMode = themeMode,
                    onThemeChange = { mainViewModel.setTheme(it) }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    currentThemeMode: String,
    onThemeChange: (String) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ZenDoRoutes.Home.route

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val showBottomNav = currentRoute in listOf(ZenDoRoutes.Home.route, ZenDoRoutes.Focus.route, ZenDoRoutes.Stats.route, ZenDoRoutes.Profile.route)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                if (!isLandscape && showBottomNav) {
                    ZenDoBottomBar(
                        currentRoute = currentRoute,
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
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
                ZenDoNavGraph(
                    navController = navController,
                    currentTheme = currentThemeMode,
                    onThemeChange = onThemeChange
                )
            }
        }
    }
}