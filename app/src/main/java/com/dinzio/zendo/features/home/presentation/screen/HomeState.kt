package com.dinzio.zendo.features.home.presentation.screen

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
    TaskUiModel("Refactor Auth Module", "4", "2", "💻"),
    TaskUiModel("Fix NullPointer in Login", "2", "0", "🐛"),
    TaskUiModel("Setup CI/CD Pipeline", "5", "3", "⚙️"),
    TaskUiModel("Learn Jetpack Compose", "6", "4", "📚"),
    TaskUiModel("Read 'Clean Code' Ch.3", "2", "2", "📖"),
    TaskUiModel("Spanish Vocab Drill", "1", "0", "🗣️"),
    TaskUiModel("Write Blog Post Draft", "3", "1", "✍️"),
    TaskUiModel("Design Home Screen UI", "4", "2", "🎨"),
    TaskUiModel("Clear Inbox (Zero Inbox)", "1", "0", "📧"),
    TaskUiModel("Weekly Review", "1", "1", "🗓️"),
    TaskUiModel("Morning Meditation", "1", "1", "🧘"),
    TaskUiModel("HIIT Workout", "2", "0", "💪"),
    TaskUiModel("Drink Water Tracking", "8", "3", "💧")
)