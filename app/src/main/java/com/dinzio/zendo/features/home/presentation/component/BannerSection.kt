package com.dinzio.zendo.features.home.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dinzio.zendo.core.presentation.components.ZenDoCurrentTaskBanner
import com.dinzio.zendo.core.service.TimerService
import com.dinzio.zendo.features.task.domain.model.TaskModel

@Composable
fun BannerSection(
    navController: NavController,
    currentTask: TaskModel?,
    roundedCornerShape: RoundedCornerShape,
) {
    AnimatedVisibility(
        visible = currentTask != null,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        val serviceState by TimerService.timerState.collectAsStateWithLifecycle()

        val formattedTime = remember(serviceState.secondsLeft) {
            val minutes = serviceState.secondsLeft / 60
            val seconds = serviceState.secondsLeft % 60
            String.format("%02d:%02d", minutes, seconds)
        }

        currentTask?.let { task ->
            Column {
                ZenDoCurrentTaskBanner(
                    taskName = task.title,
                    taskEmoji = task.icon,
                    sessionCount = task.sessionCount.toString(),
                    sessionDone = task.sessionDone.toString(),
                    remainingTime = formattedTime,
                    isRunning = serviceState.isRunning,
                    onToggleClick = {
                        if (serviceState.isRunning) {
                            TimerService.sendAction(navController.context, TimerService.ACTION_PAUSE)
                        } else {
                            val duration = if (serviceState.secondsLeft > 0)
                                serviceState.secondsLeft
                            else
                                (task.focusTime * 60L)

                            TimerService.sendAction(
                                context = navController.context,
                                action = TimerService.ACTION_START,
                                duration = duration,
                                taskId = task.id,
                                taskName = task.title
                            )
                        }
                    },
                    onClick = {
                        navController.navigate("pomodoro_task/${task.id}")
                    },
                    roundedCornerShape = roundedCornerShape,
                )
            }
        }
    }
}