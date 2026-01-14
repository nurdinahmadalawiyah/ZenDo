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
    object EditTask : ZenDoRoutes("edit_task/{taskId}") {
        fun passId(id: Int) = "edit_task/$id"
    }
    object Settings : ZenDoRoutes("settings")
    object LanguageSetting : ZenDoRoutes("settings_language")
    object ThemeSetting : ZenDoRoutes("settings_theme")
    object FocusTimerSetting : ZenDoRoutes("settings_focus_timer")
    object BreakTimerSetting : ZenDoRoutes("settings_break_timer")
    object VersionSetting : ZenDoRoutes("settings_version")
}