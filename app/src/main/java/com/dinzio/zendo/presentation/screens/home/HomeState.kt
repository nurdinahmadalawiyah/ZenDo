package com.dinzio.zendo.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.ui.graphics.vector.ImageVector

// Model Data
data class CategoryUiModel(
    val title: String,
    val count: Int,
    val icon: String
)

data class TaskUiModel(
    val title: String,
    val sessionCount: String,
    val sessionDone: String,
    val icon: String
)

// Data Dummy (Pastikan ini ada!)
val dummyCategories = listOf(
    CategoryUiModel("Hobbies", 3, "⛹\uFE0F\u200D♂\uFE0F"),
    CategoryUiModel("Study", 12, "\uD83D\uDCDA\uFE0F"),
    CategoryUiModel("Work", 8, "\uD83D\uDCBC\uFE0F"),
    CategoryUiModel("Music", 5, "⛹\uFE0F\u200D♂\uFE0F")
)

val dummyTasks = listOf(
    TaskUiModel("Learn Angular", "2 Sessions", "4 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Create Report", "1 Sessions","1 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Fix Bugs", "3 Sessions", "2 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Gym", "4 Sessions", " 1 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB")
)