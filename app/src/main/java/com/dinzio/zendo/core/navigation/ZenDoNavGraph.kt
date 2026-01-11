package com.dinzio.zendo.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dinzio.zendo.features.category.presentation.screen.CategoryScreen
import com.dinzio.zendo.features.category.presentation.screen.DetailCategoryScreen
import com.dinzio.zendo.features.home.presentation.screen.HomeScreen
import com.dinzio.zendo.features.settings.presentation.screen.LanguageSettingScreen
import com.dinzio.zendo.features.settings.presentation.screen.SettingsScreen
import com.dinzio.zendo.features.settings.presentation.screen.ThemeSettingScreen
import com.dinzio.zendo.features.task.presentation.screen.AddTaskScreen
import com.dinzio.zendo.features.task.presentation.screen.TaskScreen
import com.dinzio.zendo.features.timer.presentation.QuickTimerScreen

@Composable
fun ZenDoNavGraph(
    navController: NavHostController,
    currentTheme: String,
    onThemeChange: (String) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val bottomNavRoutes = setOf(ZenDoRoutes.Home.route, ZenDoRoutes.Focus.route, ZenDoRoutes.Stats.route, ZenDoRoutes.Profile.route)

    NavHost(
        navController = navController,
        startDestination = ZenDoRoutes.Home.route,
        enterTransition = {
            val isBottomNavSwitch = initialState.destination.route in bottomNavRoutes &&
                    targetState.destination.route in bottomNavRoutes
            if (isBottomNavSwitch) {
                fadeIn(animationSpec = tween(300))
            } else {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            }
        },
        exitTransition = {
            val isBottomNavSwitch = initialState.destination.route in bottomNavRoutes &&
                    targetState.destination.route in bottomNavRoutes
            if (isBottomNavSwitch) {
                fadeOut(animationSpec = tween(300))
            } else {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(ZenDoRoutes.Home.route) {
            HomeScreen(
                navController = navController,
            )
        }
        composable(ZenDoRoutes.Focus.route) {
            QuickTimerScreen()
        }
        composable(ZenDoRoutes.Stats.route) {
            PlaceholderScreen("Stats Screen")
        }
        composable(ZenDoRoutes.Profile.route) {
            PlaceholderScreen("Profile Screen")
        }

        composable(ZenDoRoutes.Categories.route) {
            CategoryScreen()
        }
        composable(ZenDoRoutes.DetailCategory.route) {
            DetailCategoryScreen(
                navController = navController,
            )
        }
        composable(ZenDoRoutes.Tasks.route) {
            TaskScreen(
                navController = navController,
            )
        }
        composable(ZenDoRoutes.AddTask.route) {
            AddTaskScreen(
                navController = navController,
            )
        }
        composable(ZenDoRoutes.Settings.route) {
            SettingsScreen(
                navController = navController,
                currentThemeMode = currentTheme,
                currentLanguageCode = currentLanguage,
                onThemeChange = onThemeChange,
                onLanguageChange = onLanguageChange
            )
        }
        composable(ZenDoRoutes.ThemeSetting.route) {
            ThemeSettingScreen(
                currentTheme = currentTheme,
                onThemeSelected = { newTheme ->
                    onThemeChange(newTheme)
                }
            )
        }
        composable(ZenDoRoutes.LanguageSetting.route) {
            LanguageSettingScreen(
                currentLocale = currentLanguage,
                onLanguageSelected = { newLanguage ->
                    onLanguageChange(newLanguage)
                },
            )
        }
    }
}

@Composable
fun PlaceholderScreen(screenName: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = screenName,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}