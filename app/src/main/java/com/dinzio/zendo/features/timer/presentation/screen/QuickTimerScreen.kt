@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.dinzio.zendo.features.timer.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dinzio.zendo.R
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.core.presentation.components.ZenDoCircularTimer
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.features.timer.presentation.viewModel.QuickTimerState
import com.dinzio.zendo.features.timer.presentation.viewModel.QuickTimerViewModel
import com.dinzio.zendo.features.timer.presentation.viewModel.TimerMode
import com.dinzio.zendo.features.timer.presentation.component.TimerControls
import com.dinzio.zendo.features.timer.presentation.component.TimerInfoContent

@Composable
fun QuickTimerScreen(
    viewModel: QuickTimerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isLandscapeMode = isLandscape()

    val isFocusMode = state.mode == TimerMode.FOCUS
    val modeColor = if (isFocusMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
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
            actionIcon = null,
            hideBackButton = true,
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
                .weight(0.5f)
                .fillMaxHeight()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            ZenDoTopBar(
                title = if (state.mode == TimerMode.FOCUS) stringResource(R.string.pomodoro_timer) else stringResource(R.string.break_timer),
                actionIcon = null,
                onActionClick = { },
                hideBackButton = true,
                isOnPrimaryBackground = true,
            )

            TimerInfoContent(
                headerText = uiData.headerText,
                taskText = uiData.taskText,
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
                inactiveColor = uiData.modeLightColor
            )
        }
    }
}