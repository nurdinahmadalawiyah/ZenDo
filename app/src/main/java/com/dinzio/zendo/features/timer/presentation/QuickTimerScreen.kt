@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.dinzio.zendo.features.timer.presentation

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dinzio.zendo.R
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.core.presentation.components.ZenDoCircularTimer
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar

@Composable
fun QuickTimerScreen(
    viewModel: QuickTimerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isLandscapeMode = isLandscape()

    val isFocusMode = state.mode == TimerMode.FOCUS
    val modeColor = if (isFocusMode) MaterialTheme.colorScheme.primary else Color(0xFFFF9800)
    val modeLightColor = modeColor.copy(alpha = 0.2f)

    val headerText = if (isFocusMode) stringResource(R.string.let_s_focus_for) else stringResource(R.string.take_a_break)
    val taskText = if (state.isRunning) "Focusing..." else "Ready to Focus"
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
            onReset = viewModel::stopTimer,
            onPause = viewModel::pauseTimer,
            onSkip = viewModel::skipPhase,
        )
    } else {
        TimerPhoneLayout(
            state = state,
            uiData = uiData,
            onToggle = viewModel::toggleTimer,
            onReset = viewModel::stopTimer,
            onPause = viewModel::pauseTimer,
            onSkip = viewModel::skipPhase,
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
    state: QuickTimerState,
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
            title = if (state.mode == TimerMode.FOCUS) stringResource(R.string.pomodoro_timer) else stringResource(
                R.string.break_timer
            ),
            actionIcon = Icons.Default.MoreVert,
            onActionClick = { /* Menu */ },
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        TimerInfoContent(
            headerText = uiData.headerText,
            taskText = uiData.taskText,
            emoji = uiData.taskEmoji
        )

        Spacer(modifier = Modifier.height(60.dp))

        ZenDoCircularTimer(
            totalTime = state.totalTime * 1000L,
            currentTime = state.currentTime * 1000L,
            radius = 140.dp,
            activeColor = uiData.modeColor,
            inactiveColor = uiData.modeLightColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.of_sessions, state.currentSession, state.totalSessions),
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
    state: QuickTimerState,
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
                title = if (state.mode == TimerMode.FOCUS) stringResource(R.string.pomodoro_timer) else stringResource(R.string.break_timer),
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
//                Text(
//                    text = stringResource(R.string.of_sessions, state.currentSession, state.totalSessions),
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color.Gray
//                )
//                Spacer(modifier = Modifier.height(16.dp))

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
                totalTime = state.totalTime * 1000L,
                currentTime = state.currentTime * 1000L,
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