package com.dinzio.zendo.presentation.navigation

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
import com.dinzio.zendo.presentation.screens.home.HomeScreen

@Composable
fun ZenDoNavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeSwitch: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                isDarkTheme = isDarkTheme,
                onThemeSwitch = onThemeSwitch
            )
        }
        composable("focus") {
            PlaceholderScreen("Focus Screen")
        }
        composable("stats") {
            PlaceholderScreen("Stats Screen")
        }
        composable("profile") {
            PlaceholderScreen("Profile Screen")
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