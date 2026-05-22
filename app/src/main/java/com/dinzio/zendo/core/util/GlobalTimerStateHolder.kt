package com.dinzio.zendo.core.util

import com.dinzio.zendo.features.timer.presentation.viewModel.TimerMode
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalTimerStateHolder @Inject constructor() {
    val quickTimerMode = MutableStateFlow(TimerMode.FOCUS)
}
