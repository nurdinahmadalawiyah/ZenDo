package com.dinzio.zendo.features.home.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.dinzio.zendo.core.presentation.components.ZenDoCurrentTaskBanner

@Composable
fun BannerSection() {
    Column {
        ZenDoCurrentTaskBanner(
            taskName = "Learn Angular",
            taskEmoji = "💻",
            sessionCount = "🎯 4 Sessions",
            sessionDone = "🔥 2 Done",
            onClick = {}
        )
    }
}