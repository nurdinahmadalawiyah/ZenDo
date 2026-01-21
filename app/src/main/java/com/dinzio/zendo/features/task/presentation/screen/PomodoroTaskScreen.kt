@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.dinzio.zendo.features.task.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dinzio.zendo.R
import com.dinzio.zendo.core.presentation.components.ZenDoCircularTimer
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.features.task.presentation.viewModel.pomodoroTask.PomodoroTaskState
import com.dinzio.zendo.features.task.presentation.viewModel.pomodoroTask.PomodoroTaskViewModel
import com.dinzio.zendo.features.task.presentation.viewModel.pomodoroTask.TimerMode
import com.dinzio.zendo.features.timer.presentation.component.TimerControls
import com.dinzio.zendo.features.timer.presentation.component.TimerInfoContent

data class PomodoroUiData(
    val modeColor: Color,
    val modeLightColor: Color,
    val headerText: String,
    val taskEmoji: String
)

@Composable
fun PomodoroTaskScreen(
    viewModel: PomodoroTaskViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val isLandscapeMode = isLandscape()

    val isFocusMode = state.mode == TimerMode.FOCUS
    val modeColor = if (isFocusMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    val modeLihtColor = modeColor.copy(alpha = 0.2f)

    val headerText = if (isFocusMode) stringResource(R.string.let_s_focus_for) else stringResource(R.string.take_a_break)
    val taskEmoji = if (isFocusMode) "🔥" else "☕"

    val uiData = PomodoroUiData(
        modeColor = modeColor,
        modeLightColor = modeLihtColor,
        headerText = headerText,
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
            onSkip = viewModel::skipPhase,
        )
    }
}

@Composable
fun TimerPhoneLayout(
    state: PomodoroTaskState,
    uiData: PomodoroUiData,
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
            actionIcon = Icons.Rounded.MoreVert,
            onActionClick = { },
            isOnPrimaryBackground = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        TimerInfoContent(
            headerText = uiData.headerText,
            taskText = state.taskName,
            emoji = uiData.taskEmoji
        )

        Spacer(modifier = Modifier.height(60.dp))

        ZenDoCircularTimer(
            totalTime = state.totalTime * 1000L,
            currentTime = state.currentTime * 1000L,
            radius = 140.dp,
            activeColor = uiData.modeColor,
            inactiveColor = uiData.modeLightColor,
            currentSession = state.currentSession,
            totalSession = state.totalSession
        )

        Spacer(modifier = Modifier.weight(1f))

        TimerControls(
            isRunning = state.isRunning,
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
    state: PomodoroTaskState,
    uiData: PomodoroUiData,
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
                .weight(0.5f)
                .fillMaxHeight()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            ZenDoTopBar(
                title = if (state.mode == TimerMode.FOCUS) stringResource(R.string.pomodoro_timer) else stringResource(R.string.break_timer),
                actionIcon = Icons.Rounded.MoreVert,
                onActionClick = { /* Menu */ },
                isOnPrimaryBackground = true,
            )

            TimerInfoContent(
                headerText = uiData.headerText,
                taskText = state.taskName,
                emoji = uiData.taskEmoji,
                isLarge = false
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TimerControls(
                    isRunning = state.isRunning,
                    onToggle = onToggle,
                    onPause = onPause,
                    onReset = onReset,
                    onSkip = onSkip
                )
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            val minDimension = minOf(maxWidth, maxHeight)
            val dynamicRadius = (minDimension / 2) * 0.8f

            ZenDoCircularTimer(
                totalTime = state.totalTime * 1000L,
                currentTime = state.currentTime * 1000L,
                radius = dynamicRadius,
                activeColor = uiData.modeColor,
                inactiveColor = uiData.modeLightColor,
                currentSession = state.currentSession,
                totalSession = state.totalSession
            )
        }
    }
}