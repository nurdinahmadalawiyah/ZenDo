package com.dinzio.zendo.presentation.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.domain.usecase.timer.GetTimerSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickTimerViewModel @Inject constructor(
    private val getTimerSettingsUseCase: GetTimerSettingsUseCase // Inject UseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private var endTime: Long = 0L

    // Variable ini sekarang akan diisi oleh UseCase
    private var focusTimeSettings = 25 * 60 * 1000L
    private var breakTimeSettings = 5 * 60 * 1000L

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            getTimerSettingsUseCase().collect { settings ->
                focusTimeSettings = settings.focusDuration
                breakTimeSettings = settings.breakDuration

                // Update State Awal sesuai Data dari Repository
                _state.update {
                    it.copy(
                        totalTime = focusTimeSettings,
                        currentTime = focusTimeSettings,
                        totalSessions = settings.totalSessions
                    )
                }
            }
        }
    }

    fun toggleTimer() {
        if (_state.value.isRunning) pauseTimer() else startTimer()
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return

        // Logika Anti-Drift (Best Practice)
        endTime = System.currentTimeMillis() + _state.value.currentTime
        _state.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (_state.value.isRunning && _state.value.currentTime > 0) {
                val remainingTime = endTime - System.currentTimeMillis()
                if (remainingTime <= 0) {
                    _state.update { it.copy(currentTime = 0) }
                    handleTimerFinished()
                    break
                } else {
                    _state.update { it.copy(currentTime = remainingTime) }
                }
                delay(50)
            }
        }
    }

    private fun handleTimerFinished() {
        _state.update { currentState ->
            val nextMode = if (currentState.mode == TimerMode.FOCUS) TimerMode.SHORT_BREAK else TimerMode.FOCUS

            // Menggunakan durasi dari variable settings yang diambil dari Repo
            val nextTime = if (nextMode == TimerMode.FOCUS) focusTimeSettings else breakTimeSettings

            val nextSession = if (nextMode == TimerMode.FOCUS) currentState.currentSession else currentState.currentSession + 1

            currentState.copy(
                isRunning = false,
                mode = nextMode,
                totalTime = nextTime,
                currentTime = nextTime,
                currentSession = nextSession
            )
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _state.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        // Reset ke totalTime saat ini (entah itu Focus atau Break)
        _state.update { it.copy(currentTime = it.totalTime, isRunning = false) }
    }

    fun skipPhase() {
        timerJob?.cancel()
        handleTimerFinished()
    }
}