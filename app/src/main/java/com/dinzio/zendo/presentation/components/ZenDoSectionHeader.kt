package com.dinzio.zendo.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.core.theme.BlackText
import com.dinzio.zendo.core.theme.GreenPrimary

@Composable
fun ZenDoSectionHeader(
    title: String,
    actionText: String = "See all",
    showActionText: Boolean = true,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = BlackText
        )

        if (showActionText) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = GreenPrimary,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ZenDoTaskItemCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ZenDoSectionHeader(title = "Task List", onActionClick = {})
    }
}