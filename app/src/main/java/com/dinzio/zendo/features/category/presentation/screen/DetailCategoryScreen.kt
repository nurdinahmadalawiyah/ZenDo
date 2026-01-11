package com.dinzio.zendo.features.category.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.core.theme.White
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.core.presentation.components.ZenDoSectionHeader
import com.dinzio.zendo.core.presentation.components.ZenDoTaskItemCard
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.navigation.ZenDoRoutes
import com.dinzio.zendo.features.home.presentation.screen.TaskUiModel

@Composable
fun DetailCategoryScreen(
    categoryName: String = "Productivity",
    navController: NavController,
) {
    val isLandscapeMode = isLandscape()

    val onAddTaskClick = {
        navController.navigate(ZenDoRoutes.AddTask.route)
    }

    if (isLandscapeMode) {
        DetailCategoryTabletLayout(
            categoryName = categoryName,
            onAddTaskClick = onAddTaskClick
        )
    } else {
        DetailCategoryPhoneLayout(
            categoryName = categoryName,
            onAddTaskClick = onAddTaskClick
        )
    }
}

@Composable
fun DetailCategoryPhoneLayout(
    categoryName: String,
    onAddTaskClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            ZenDoTopBar(
                title = categoryName,
                actionIcon = Icons.Default.Add,
                onActionClick = onAddTaskClick,
                isOnPrimaryBackground = false
            )
        }

        Box(modifier = Modifier.padding(16.dp)) {
            CategoryInfoCard()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            ZenDoSectionHeader(title = "Task List", showActionText = false, onActionClick = {})

            Spacer(modifier = Modifier.height(16.dp))

            TaskList(onItemClick = onAddTaskClick)
        }
    }
}

@Composable
fun DetailCategoryTabletLayout(
    categoryName: String,
    onAddTaskClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .padding(16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp)
        ) {
            ZenDoTopBar(
                title = categoryName,
                actionIcon = Icons.Default.Add,
                onActionClick = onAddTaskClick,
                isOnPrimaryBackground = false
            )

            Spacer(modifier = Modifier.height(32.dp))

            CategoryInfoCard(tabletMode = true)
        }

        Column(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            ZenDoSectionHeader(title = "Task List", showActionText = false, onActionClick = {})

            Spacer(modifier = Modifier.height(16.dp))

            TaskList(onItemClick = onAddTaskClick)
        }
    }
}

@Composable
fun TaskList(onItemClick: () -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(dummyDetailTasks) { task ->
            ZenDoTaskItemCard(
                title = task.title,
                sessionCount = task.sessionCount,
                sessionDone = task.sessionDone,
                categoryIcon = task.icon,
                onItemClick = onItemClick,
                onLongItemClick = { },
                onPlayClick = onItemClick
            )
        }
    }
}

@Composable
fun CategoryInfoCard(
    tabletMode: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (tabletMode) {
                    Modifier.fillMaxHeight()
                } else {
                    Modifier.height(140.dp)
                }
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = if (tabletMode) Arrangement.Center else Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = if (tabletMode) Modifier.fillMaxWidth() else Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = if (tabletMode) Alignment.CenterHorizontally else Alignment.Start
            ) {
                Text(
                    text = "Productivity",
                    style = if (tabletMode) MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ) else MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🚀 3 Task",
                    style = if (tabletMode) MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium) else MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = White
                )

                if (tabletMode) {
                    Spacer(modifier = Modifier.height(64.dp))
                    Text(
                        text = "\uD83D\uDCDA\uFE0F",
                        fontSize = 200.sp,
                    )
                }
            }

            if (!tabletMode) {
                Text(
                    text = "\uD83D\uDCDA\uFE0F",
                    fontSize = 64.sp,
                )
            }
        }
    }
}

val dummyDetailTasks = listOf(
    TaskUiModel("Learn Angular", "2 Sessions", "4 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Create Report", "1 Sessions", "1 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Fix Bugs", "3 Sessions", "2 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel("Gym", "4 Sessions", " 1 Done", "\uD83E\uDDD1\u200D\uD83D\uDCBB"),
    TaskUiModel(
        "Reading",
        "1 Sessions",
        " 0 Done",
        "\uD83D\uDCD6"
    ),
    TaskUiModel("Meditation", "1 Sessions", " 1 Done", "\uD83E\uDDD8\u200D\u2642\uFE0F")
)

@Preview(name = "Phone Portrait", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewDetailPhone() {
    DetailCategoryScreen(
        navController = rememberNavController(),
    )
}

@Preview(
    name = "Tablet Landscape",
    showBackground = true,
    device = "spec:width=800dp,height=1280dp,dpi=320,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun PreviewDetailTablet() {
    DetailCategoryScreen(
        navController = rememberNavController(),
    )
}