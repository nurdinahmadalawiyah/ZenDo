package com.dinzio.zendo.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dinzio.zendo.core.theme.BackgroundGray
import com.dinzio.zendo.core.theme.BlackText
import com.dinzio.zendo.core.theme.GrayText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZenDoCategoryCard(
    title: String,
    taskCount: Int,
    icon: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .width(160.dp)
            .height(170.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundGray
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = BlackText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "🚀 $taskCount Task",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayText
                )
            }

            Text(
                text = icon,
                fontSize = 64.sp,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ZenDoCategoryCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ZenDoCategoryCard(
            title = "Hobbies",
            taskCount = 3,
            icon = "⛹\uFE0F\u200D♂\uFE0F",
            onClick = {}
        )
    }
}