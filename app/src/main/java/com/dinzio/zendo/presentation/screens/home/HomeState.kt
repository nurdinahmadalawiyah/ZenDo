package com.dinzio.zendo.presentation.screens.home

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
    CategoryUiModel("Deep Work", 8, "🧠"),      // Fokus tinggi, tanpa gangguan
    CategoryUiModel("Study", 12, "📚"),         // Belajar umum
    CategoryUiModel("Coding", 15, "💻"),        // Programming
    CategoryUiModel("Writing", 5, "✍️"),        // Menulis artikel/skripsi
    CategoryUiModel("Design", 6, "🎨"),         // UI/UX, Gambar
    CategoryUiModel("Languages", 4, "🗣️"),      // Belajar bahasa asing
)

val dummyTasks = listOf(
    TaskUiModel("Refactor Auth Module", "4 Sessions", "2 Done", "💻"),
    TaskUiModel("Fix NullPointer in Login", "2 Sessions", "0 Done", "🐛"),
    TaskUiModel("Setup CI/CD Pipeline", "5 Sessions", "3 Done", "⚙️"),
    TaskUiModel("Learn Jetpack Compose", "6 Sessions", "4 Done", "📚"),
    TaskUiModel("Read 'Clean Code' Ch.3", "2 Sessions", "2 Done", "📖"),
    TaskUiModel("Spanish Vocab Drill", "1 Sessions", "0 Done", "🗣️"),
    TaskUiModel("Write Blog Post Draft", "3 Sessions", "1 Done", "✍️"),
    TaskUiModel("Design Home Screen UI", "4 Sessions", "2 Done", "🎨"),
    TaskUiModel("Clear Inbox (Zero Inbox)", "1 Sessions", "0 Done", "📧"),
    TaskUiModel("Weekly Review", "1 Sessions", "1 Done", "🗓️"),
    TaskUiModel("Morning Meditation", "1 Sessions", "1 Done", "🧘"),
    TaskUiModel("HIIT Workout", "2 Sessions", "0 Done", "💪"),
    TaskUiModel("Drink Water Tracking", "8 Sessions", "3 Done", "💧")
)