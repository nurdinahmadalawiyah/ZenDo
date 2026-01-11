package com.dinzio.zendo.core.navigation

sealed class ZenDoRoutes(val route: String) {
    object Home : ZenDoRoutes("home")
    object Focus : ZenDoRoutes("focus")
    object Stats : ZenDoRoutes("stats")
    object Profile : ZenDoRoutes("profile")
    object Categories : ZenDoRoutes("categories")
    object DetailCategory : ZenDoRoutes("detail_category")
    object Tasks : ZenDoRoutes("tasks")
    object AddTask : ZenDoRoutes("add_task")
    object Settings : ZenDoRoutes("settings")
    object LanguageSetting : ZenDoRoutes("settings_language")
    object ThemeSetting : ZenDoRoutes("settings_theme")
}