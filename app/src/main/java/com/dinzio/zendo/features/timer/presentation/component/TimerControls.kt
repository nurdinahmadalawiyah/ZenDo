package com.dinzio.zendo.features.timer.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimerControls(
    isRunning: Boolean,
    onToggle: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().animateContentSize()
    ) {
        AnimatedVisibility(
            visible = isRunning,
            enter = fadeIn() + scaleIn(
                initialScale = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = fadeOut() + scaleOut(targetScale = 0f)
        ) {
            Row {
                TimerControlButton(
                    icon = Icons.Rounded.Refresh,
                    size = 60.dp,
                    backgroundColor = MaterialTheme.colorScheme.error,
                    onClick = onReset
                )
                Spacer(modifier = Modifier.width(24.dp))
            }
        }

        Box(contentAlignment = Alignment.Center) {
            TimerControlButton(
                icon = if (isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                backgroundColor = if (isRunning) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                onClick = if (isRunning) onPause else onToggle
            )
        }

        AnimatedVisibility(
            visible = isRunning,
            enter = fadeIn() + scaleIn(
                initialScale = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = fadeOut() + scaleOut(targetScale = 0f)
        ) {
            Row {
                Spacer(modifier = Modifier.width(24.dp))
                TimerControlButton(
                    icon = Icons.Rounded.SkipNext,
                    size = 60.dp,
                    backgroundColor = MaterialTheme.colorScheme.error,
                    onClick = onSkip
                )
            }
        }
    }
}