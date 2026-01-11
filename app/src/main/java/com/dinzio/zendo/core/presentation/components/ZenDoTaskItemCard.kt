package com.dinzio.zendo.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ZenDoTaskItemCard(
    title: String,
    sessionCount: String,
    sessionDone: String,
    categoryIcon: String,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    onLongItemClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = onLongItemClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryIcon,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "\uD83C\uDFAF $sessionCount • \uD83D\uDD25 $sessionDone",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            CircleButton(
                icon = Icons.Default.PlayArrow,
                onClick = onPlayClick,
                size = 36.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ZenDoTaskItemCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ZenDoTaskItemCard(
            title = "Learn Jetpack Compose",
            sessionCount = "2 Sessions",
            sessionDone = "4 Done",
            categoryIcon = "\uD83E\uDDD1\u200D\uD83D\uDCBB",
            onItemClick = {},
            onLongItemClick = {},
            onPlayClick = {}
        )
    }
}