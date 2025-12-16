package com.dinzio.zendo.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.dinzio.zendo.presentation.components.ZenDoBottomBar
import com.dinzio.zendo.presentation.components.ZenDoNavigationRail
import com.dinzio.zendo.presentation.navigation.ZenDoNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme by mainViewModel.isDarkMode.collectAsState()

            ZendoTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false
            ) {
                MainScreen(isDarkTheme) { mainViewModel.toggleTheme(it) }
            }
        }
    }
}

@Composable
fun MainScreen(isDarkTheme: Boolean, onThemeSwitch: (Boolean) -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val showBottomNav = currentRoute in listOf("home", "focus", "stats", "profile")

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
                    isDarkTheme = isDarkTheme,
                    onThemeSwitch = onThemeSwitch
                )
            }
        }
    }
}
