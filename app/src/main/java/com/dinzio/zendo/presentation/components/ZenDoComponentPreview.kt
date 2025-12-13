package com.dinzio.zendo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, heightDp = 1200)
@Composable
fun ZenDoComponentsPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // --- 1. NEW: CUSTOM TOP BAR ---
        Text("1. Top Bar", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        ZenDoTopBar(
            title = "Detail Task",
            onBackClick = {},
            actionIcon = Icons.Default.MoreVert,
            onActionClick = {}
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. NEW: HERO BANNER ---
        Text("2. Home Banner", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        ZenDoCurrentTaskBanner(
            taskName = "Learn Angular",
            taskEmoji = "\uD83E\uDDD1\u200D\uD83D\uDCBB",
            sessionCount = "\uD83C\uDFAF 4 Sessions",
            sessionDone = "\uD83D\uDD25 2 Done",
            onClick = {}
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("3. Input & Search", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        ZenDoInput(
            value = "",
            onValueChange = {},
            placeholder = "Search Category...",
            leadingIcon = Icons.Default.Search
        )

        Spacer(modifier = Modifier.height(16.dp))

        ZenDoInput(
            value = "Belajar Jetpack Compose",
            onValueChange = {},
            placeholder = "Task Name"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- 4. EXISTING: BUTTONS ---
        Text("4. Buttons", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        CircleButton(
            icon = Icons.Default.PlayArrow,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- 5. EXISTING: CATEGORY CARDS ---
        Text("5. Category Cards", color = Color.Gray)
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            ZenDoCategoryCard(
                title = "Hobbies",
                taskCount = 3,
                icon = "⛹\uFE0F\u200D♂\uFE0F",
                onClick = {}
            )

            Spacer(modifier = Modifier.width(16.dp))

            ZenDoCategoryCard(
                title = "Study",
                taskCount = 12,
                icon = "⛹\uFE0F\u200D♂\uFE0F",
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 6. EXISTING: LIST ITEMS & HEADER ---
        Text("6. List Items", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        ZenDoSectionHeader(title = "Task List", onActionClick = {})

        Spacer(modifier = Modifier.height(10.dp))

        ZenDoTaskItemCard(
            title = "Learn Jetpack Compose",
            sessionCount = "2 Sessions",
            sessionDone = "4 Done",
            categoryIcon = "\uD83E\uDDD1\u200D\uD83D\uDCBB",
            onItemClick = {},
            onPlayClick = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        ZenDoTaskItemCard(
            title = "Create Report",
            sessionCount = "2 Sessions",
            sessionDone = "4 Done",
            categoryIcon = "\uD83E\uDDD1\u200D\uD83D\uDCBB",
            onItemClick = {},
            onPlayClick = {}
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- 7. NEW: BOTTOM NAVIGATION ---
        Text("7. Bottom Navigation", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        ZenDoBottomBar(
            currentRoute = "home",
            onNavigate = {}
        )

        Spacer(modifier = Modifier.height(50.dp)) // Extra space di bawah
    }
}