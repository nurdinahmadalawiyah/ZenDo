@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.dinzio.zendo.presentation.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.presentation.components.ZenDoCircularTimer
import com.dinzio.zendo.presentation.components.ZenDoTopBar

@Composable
fun QuickTimerScreen(
    viewModel: QuickTimerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isLandscapeMode = isLandscape()

    val isFocusMode = state.mode == TimerMode.FOCUS

    val modeColor = if (isFocusMode) MaterialTheme.colorScheme.primary else Color(0xFFFF9800)
    val modeLightColor = modeColor.copy(alpha = 0.2f)

    val headerText = if (isFocusMode) "Let's focus for" else "Take a break"
    val taskText = if (isFocusMode) "Learn React" else "Relaxing"
    val taskEmoji = if (isFocusMode) "🔥" else "☕"

    val uiData = TimerUiData(
        modeColor = modeColor,
        modeLightColor = modeLightColor,
        headerText = headerText,
        taskText = taskText,
        taskEmoji = taskEmoji
    )

    if (isLandscapeMode) {
        TimerTabletLayout(
            state = state,
            uiData = uiData,
            onToggle = viewModel::toggleTimer,
            onReset = viewModel::resetTimer,
            onPause = viewModel::pauseTimer,
            onSkip = viewModel::skipPhase,
        )
    } else {
        TimerPhoneLayout(
            state = state,
            uiData = uiData,
            onToggle = viewModel::toggleTimer,
            onReset = viewModel::resetTimer,
            onPause = viewModel::pauseTimer,
            onSkip = viewModel::skipPhase
        )
    }
}

data class TimerUiData(
    val modeColor: Color,
    val modeLightColor: Color,
    val headerText: String,
    val taskText: String,
    val taskEmoji: String
)

@Composable
fun TimerPhoneLayout(
    state: TimerState,
    uiData: TimerUiData,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    onPause: () -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ZenDoTopBar(
            title = if (state.mode == TimerMode.FOCUS) "Pomodoro Timer" else "Break Timer",
            actionIcon = Icons.Default.MoreVert,
            onActionClick = { /* Menu */ },
            isOnPrimaryBackground = false
        )

        Spacer(modifier = Modifier.height(40.dp))

        TimerInfoContent(
            headerText = uiData.headerText,
            taskText = uiData.taskText,
            emoji = uiData.taskEmoji
        )

        Spacer(modifier = Modifier.height(60.dp))

        ZenDoCircularTimer(
            totalTime = state.totalTime,
            currentTime = state.currentTime,
            radius = 140.dp,
            activeColor = uiData.modeColor,
            inactiveColor = uiData.modeLightColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🎯 ${state.currentSession} of ${state.totalSessions} sessions",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.weight(1f))

        TimerControls(
            isRunning = state.isRunning,
            mainColor = uiData.modeColor,
            onToggle = onToggle,
            onPause = onPause,
            onReset = onReset,
            onSkip = onSkip
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun TimerTabletLayout(
    state: TimerState,
    uiData: TimerUiData,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    onPause: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            ZenDoTopBar(
                title = if (state.mode == TimerMode.FOCUS) "Pomodoro Timer" else "Break Timer",
                actionIcon = Icons.Default.MoreVert,
                onActionClick = { /* Menu */ },
                isOnPrimaryBackground = true,
            )

            TimerInfoContent(
                headerText = uiData.headerText,
                taskText = uiData.taskText,
                emoji = uiData.taskEmoji,
                isLarge = false
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🎯 ${state.currentSession} of ${state.totalSessions} sessions",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                TimerControls(
                    isRunning = state.isRunning,
                    mainColor = uiData.modeColor,
                    onToggle = onToggle,
                    onPause = onPause,
                    onReset = onReset,
                    onSkip = onSkip
                )
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            val minDimension = minOf(maxWidth, maxHeight)
            val dynamicRadius = (minDimension / 2) * 0.8f

            val dynamicStroke = if (minDimension < 360.dp) 12.dp else 24.dp

            ZenDoCircularTimer(
                totalTime = state.totalTime,
                currentTime = state.currentTime,
                radius = dynamicRadius,
                strokeWidth = dynamicStroke,
                activeColor = uiData.modeColor,
                inactiveColor = uiData.modeLightColor
            )
        }
    }
}

@Composable
fun TimerInfoContent(
    headerText: String,
    taskText: String,
    emoji: String,
    isLarge: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = headerText, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = taskText,
                style = if (isLarge)
                    MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
                else
                    MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(text = " $emoji", fontSize = if (isLarge) 32.sp else 24.sp)
        }
    }
}

@Composable
fun TimerControls(
    isRunning: Boolean,
    mainColor: Color,
    onToggle: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isRunning) {
            TimerControlButton(
                icon = Icons.Default.Pause,
                backgroundColor = Color(0xFFFFC107),
                onClick = onPause
            )

            Spacer(modifier = Modifier.width(24.dp))

            TimerControlButton(
                icon = Icons.Default.Refresh,
                backgroundColor = Color(0xFFD32F2F),
                onClick = onReset
            )

            Spacer(modifier = Modifier.width(24.dp))

            TimerControlButton(
                icon = Icons.Default.SkipNext,
                backgroundColor = Color(0xFFD32F2F),
                onClick = onSkip
            )
        } else {
            TimerControlButton(
                icon = Icons.Default.PlayArrow,
                backgroundColor = mainColor,
                size = 80.dp,
                onClick = onToggle
            )
        }
    }
}

@Composable
fun TimerControlButton(
    icon: ImageVector,
    backgroundColor: Color,
    size: Dp = 64.dp,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

//@Preview(name = "Phone Portrait", showBackground = true, device = Devices.PIXEL_4)
//@Composable
//fun PreviewTimerPhone() {
//    TimerPhoneLayout(
//        navController = rememberNavController(),
//        state = TimerState(),
//        uiData = TimerUiData,
//        onToggle = {}, onReset = {}, onPause = {}, onSkip = {}
//    )
//}
//
//@Preview(
//    name = "Tablet Landscape",
//    showBackground = true,
//    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=landscape"
//)
//@Composable
//fun PreviewTimerTablet() {
//    TimerTabletLayout(
//        navController = rememberNavController(),
//        state = TimerState(),
//        onToggle = {}, onReset = {}, onPause = {}
//    )
//}