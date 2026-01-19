package com.dinzio.zendo.features.timer.presentation

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
    }

    val state = combine(
        _selectedMinutes,
        _currentMode,
        TimerService.timerState,
    ) { selectedMinutes, mode, serviceState ->

        val totalSeconds = (selectedMinutes * 60).toLong()

        val displayTime = if (serviceState.secondsLeft > 0) {
            serviceState.secondsLeft
        } else {
            totalSeconds
        }

        QuickTimerState(
            selectedMinutes = selectedMinutes,
            timerServiceState = serviceState,
            isSettingTimer = !serviceState.isRunning && serviceState.secondsLeft <= 0L,
            mode = mode,
            currentTime = displayTime,
            totalTime = totalSeconds,
            isRunning = serviceState.isRunning,
            currentSession = 1,
            totalSessions = 4
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = QuickTimerState()
    )

    fun onDurationChange(minutes: Int) {
        _selectedMinutes.update { minutes }
    }

    fun toggleTimer() {
        if (state.value.isRunning) pauseTimer() else startTimer()
    }

    fun startTimer() {
        val serviceState = state.value.timerServiceState
        val timeLeft = serviceState.secondsLeft

        val duration = if (timeLeft > 0) {
            timeLeft
        } else {
            (_selectedMinutes.value * 60).toLong()
        }

        TimerService.sendAction(application, TimerService.ACTION_START, duration)
    }

    fun pauseTimer() {
        TimerService.sendAction(application, TimerService.ACTION_PAUSE)
    }

    fun stopTimer() {
        TimerService.sendAction(application, TimerService.ACTION_STOP)
    }

    fun skipPhase() {
        stopTimer()

        val nextMode = if (_currentMode.value == TimerMode.FOCUS) {
            TimerMode.BREAK
        } else {
            TimerMode.FOCUS
        }

        _currentMode.value = nextMode
    }
}