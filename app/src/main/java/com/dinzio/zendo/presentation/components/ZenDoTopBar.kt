package com.dinzio.zendo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dinzio.zendo.core.theme.BlackText
import com.dinzio.zendo.core.theme.GreenPrimary

@Composable
fun ZenDoTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionIcon: ImageVector? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(GreenPrimary)
                .height(36.dp)
                .width(36.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = BlackText,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        if (actionIcon != null && onActionClick != null) {
            IconButton(
                onClick = { onActionClick() },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(GreenPrimary)
                    .height(36.dp)
                    .width(36.dp)
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = "Action",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(4.dp),
                )
            }
        } else {
            Box(modifier = Modifier.padding(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ZenDoTopBarPreview() {
    ZenDoTopBar(
        title = "Detail Task",
        onBackClick = {},
        actionIcon = Icons.Default.MoreVert,
        onActionClick = {}
    )
}