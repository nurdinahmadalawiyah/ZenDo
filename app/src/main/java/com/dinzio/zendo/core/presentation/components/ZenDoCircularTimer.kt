package com.dinzio.zendo.core.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dinzio.zendo.R
import com.dinzio.zendo.core.util.toTimerString

@Composable
fun ZenDoCircularTimer(
    totalTime: Long,
    currentTime: Long,
    modifier: Modifier = Modifier,
    radius: Dp = 140.dp,
    strokeWidth: Dp = 16.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    currentSession: Int? = null,
    totalSession: Int? = null,
) {
    val formattedTime = currentTime.toTimerString()

    val targetProgress = if (totalTime > 0) currentTime.toFloat() / totalTime.toFloat() else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "TimerProgress"
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(radius * 2)
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = activeColor,
            strokeWidth = strokeWidth,
            trackColor = inactiveColor,
            strokeCap = StrokeCap.Round,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 56.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            if (currentSession != null && totalSession != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.of_sessions, currentSession, totalSession),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimer() {
    ZenDoCircularTimer(
        totalTime = 100000L,
        currentTime = 65000L,
        currentSession = 2,
        totalSession = 5
    )
}