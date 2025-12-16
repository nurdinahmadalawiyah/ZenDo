package com.dinzio.zendo.presentation.navigation

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
import com.dinzio.zendo.presentation.screens.category.CategoryScreen
import com.dinzio.zendo.presentation.screens.category.DetailCategoryScreen
import com.dinzio.zendo.presentation.screens.home.HomeScreen
import com.dinzio.zendo.presentation.screens.task.TaskScreen

@Composable
fun ZenDoNavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeSwitch: (Boolean) -> Unit
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
                isDarkTheme = isDarkTheme,
                onThemeSwitch = onThemeSwitch,
            )
        }
        composable(ZenDoRoutes.Focus.route) {
            PlaceholderScreen("Focus Screen")
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
            TaskScreen()
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