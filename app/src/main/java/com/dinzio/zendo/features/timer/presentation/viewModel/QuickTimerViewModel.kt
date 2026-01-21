package com.dinzio.zendo.features.timer.presentation.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.core.data.local.BreakTimerManager
import com.dinzio.zendo.core.data.local.FocusTimerManager
import com.dinzio.zendo.core.service.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickTimerViewModel @Inject constructor(
    private val application: Application,
    private val focusTimerManager: FocusTimerManager,
    private val breakTimerManager: BreakTimerManager
) : ViewModel() {

    private val _selectedMinutes = MutableStateFlow(25)
    private val _currentMode = MutableStateFlow(TimerMode.FOCUS)
    private val focusTimeState = focusTimerManager.focusTime
        .stateIn(viewModelScope, SharingStarted.Eagerly, 25)
    private val breakTimeState = breakTimerManager.breakTime
        .stateIn(viewModelScope, SharingStarted.Eagerly, 5)

    init {
        viewModelScope.launch {
            combine(
                focusTimerManager.focusTime,
                breakTimerManager.breakTime,
                _currentMode
            ) { focus, breakTime, mode ->
                if (mode == TimerMode.FOCUS) focus else breakTime
            }.collect { time ->
                _selectedMinutes.value = time
            }
        }

        viewModelScope.launch {
            TimerService.timerState.collect { serviceState ->
                if (serviceState.isFinished && serviceState.currentTaskId == null) {
                    handleTimerFinished()
                }
            }
        }
    }

    private fun handleTimerFinished() {
        TimerService.sendAction(application, TimerService.ACTION_STOP)

        val nextMode = if (_currentMode.value == TimerMode.FOCUS) TimerMode.BREAK else TimerMode.FOCUS
        _currentMode.value = nextMode

        val nextMinutes = if (nextMode == TimerMode.FOCUS) {
            focusTimeState.value
        } else {
            breakTimeState.value
        }

        TimerService.sendAction(
            context = application,
            action = TimerService.ACTION_START,
            duration = (nextMinutes * 60).toLong(),
            taskId = null
        )
    }

    val state = combine(
        _selectedMinutes,
        _currentMode,
        TimerService.timerState,
    ) { minutes, mode, serviceState ->

        val isMine = serviceState.currentTaskId == null
        val totalSeconds = (minutes * 60).toLong()
        val displayTime = if (isMine && serviceState.secondsLeft > 0) {
            serviceState.secondsLeft
        } else {
            totalSeconds
        }

        QuickTimerState(
            selectedMinutes = minutes,
            timerServiceState = serviceState,
            isSettingTimer = !isMine || (!serviceState.isRunning && serviceState.secondsLeft <= 0L),
            mode = mode,
            currentTime = displayTime,
            totalTime = totalSeconds,
            isRunning = serviceState.isRunning && isMine,
            currentSession = 1,
            totalSessions = 4
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = QuickTimerState()
    )

    fun toggleTimer() {
        if (state.value.isRunning) pauseTimer() else startTimer()
    }

    fun startTimer() {
        val serviceState = TimerService.timerState.value
        val isSameTimer = serviceState.currentTaskId == null

        val duration = if (isSameTimer && serviceState.secondsLeft > 0) {
            serviceState.secondsLeft
        } else {
            (_selectedMinutes.value * 60).toLong()
        }

        TimerService.sendAction(
            context = application,
            action = TimerService.ACTION_START,
            duration = duration,
            taskId = null
        )
    }

    fun pauseTimer() {
        TimerService.sendAction(application, TimerService.ACTION_PAUSE)
    }

    fun resetTimer() {
        TimerService.sendAction(application, TimerService.ACTION_STOP)
        _currentMode.value = TimerMode.FOCUS
    }

    fun skipPhase() {
        resetTimer()
        _currentMode.update {
            if (it == TimerMode.FOCUS) TimerMode.BREAK else TimerMode.FOCUS
        }
        startTimer()
    }
}