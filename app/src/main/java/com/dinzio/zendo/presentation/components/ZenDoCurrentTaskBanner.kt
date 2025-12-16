package com.dinzio.zendo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dinzio.zendo.core.theme.OrangeAccent
import com.dinzio.zendo.core.theme.White

@Composable
fun ZenDoCurrentTaskBanner(
    taskName: String,
    taskEmoji: String,
    sessionCount: String,
    sessionDone: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = taskEmoji,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Current Task",
                    style = MaterialTheme.typography.labelMedium,
                    color = White.copy(alpha = 0.7f)
                )
                Text(
                    text = taskName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = White
                )
                Text(
                    text = "$sessionCount • $sessionDone",
                    style = MaterialTheme.typography.bodySmall,
                    color = White.copy(alpha = 0.9f)
                )
            }
            
            CircleButton(
                icon = Icons.Default.PlayArrow,
                onClick = { /* Start Timer Directly */ },
                backgroundColor = OrangeAccent,
                iconColor = White,
                size = 40.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ZenDoCurrentTaskBannerPreview() {
    ZenDoCurrentTaskBanner(
        taskName = "Learn Angular",
        taskEmoji = "\uD83E\uDDD1\u200D\uD83D\uDCBB",
        sessionCount = "\uD83C\uDFAF 4 Sessions",
        sessionDone = "\uD83D\uDD25 2 Done",
        onClick = {}
    )
}