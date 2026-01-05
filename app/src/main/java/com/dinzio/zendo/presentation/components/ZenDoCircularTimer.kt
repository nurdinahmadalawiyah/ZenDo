package com.dinzio.zendo.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dinzio.zendo.core.util.toTimerString

@Composable
fun ZenDoCircularTimer(
    totalTime: Long,
    currentTime: Long,
    modifier: Modifier = Modifier,
    radius: Dp = 140.dp,
    strokeWidth: Dp = 16.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
) {
    val progress = if (totalTime > 0) currentTime.toFloat() / totalTime.toFloat() else 0f

    val formattedTime = currentTime.toTimerString()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(radius * 2)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = activeColor,
            strokeWidth = strokeWidth,
            trackColor = inactiveColor,
            strokeCap = StrokeCap.Round,
        )

        Text(
            text = formattedTime,
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 56.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimer() {
    ZenDoCircularTimer(
        totalTime = 100000L,
        currentTime = 65000L
    )
}