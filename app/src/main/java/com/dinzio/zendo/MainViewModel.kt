package com.dinzio.zendo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.core.data.local.BreakTimerManager
import com.dinzio.zendo.core.data.local.FocusTimerManager
import com.dinzio.zendo.core.data.local.LanguageManager
import com.dinzio.zendo.core.data.local.ThemeManager
import com.dinzio.zendo.core.service.TimerService
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.domain.usecase.GetTaskByIdUseCase
import com.dinzio.zendo.features.task.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeManager: ThemeManager,
    private val languageManager: LanguageManager,
    private val focusTimerManager: FocusTimerManager,
    private val breakTimerManager: BreakTimerManager,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModel() {

    val isTaskFinishedGlobal = TimerService.timerState.map { it.isFinished }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val themeMode: StateFlow<String> = themeManager.themeMode.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "system"
        )

    fun setTheme(mode: String) {
        viewModelScope.launch {
            themeManager.setThemeMode(mode)
        }
    }

    val languageCode: StateFlow<String> = languageManager.languageCode.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "system"
        )

    fun setLanguage(code: String) {
        viewModelScope.launch {
            languageManager.setLanguage(code)
        }
    }

    val focusTime: StateFlow<Int> = focusTimerManager.focusTime.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 25
        )

    fun setFocusTime(time: Int) {
        viewModelScope.launch {
            focusTimerManager.setFocusTime(time)
        }
    }

    val breakTime: StateFlow<Int> = breakTimerManager.breakTime.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 5
        )

    fun setBreakTime(time: Int) {
        viewModelScope.launch {
            breakTimerManager.setBreakTime(time)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentTaskBannerState = TimerService.timerState.flatMapLatest { serviceState ->
        flow {
            if (serviceState.currentTaskId != null) {
                val task = getTaskByIdUseCase(serviceState.currentTaskId)
                emit(task)
            } else {
                emit(null)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun completeAndResetTask(context: android.content.Context, task: TaskModel?) {
        viewModelScope.launch {
            task?.let {
                val completedTask = it.copy(
                    isCompleted = true,
                    sessionDone = it.sessionCount,
                    lastSecondsLeft = 0L
                )
                updateTaskUseCase(completedTask)
            }
            TimerService.sendAction(context, TimerService.ACTION_STOP)
        }
    }
}