package com.dinzio.zendo.presentation.navigation

sealed class ZenDoRoutes(val route: String) {
    object Home : ZenDoRoutes("home")
    object Focus : ZenDoRoutes("focus")
    object Stats : ZenDoRoutes("stats")
    object Profile : ZenDoRoutes("profile")
    object Categories : ZenDoRoutes("categories")
    object DetailCategory : ZenDoRoutes("detail_category")
    object AddTask : ZenDoRoutes("add_task")
}