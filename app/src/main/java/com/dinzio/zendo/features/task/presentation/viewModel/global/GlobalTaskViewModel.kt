package com.dinzio.zendo.features.task.presentation.viewModel.global

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.core.service.TimerService
import com.dinzio.zendo.core.util.GlobalTimerStateHolder
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.domain.usecase.GetTaskByIdUseCase
import com.dinzio.zendo.features.task.domain.usecase.UpdateTaskUseCase
import com.dinzio.zendo.features.timer.presentation.viewModel.TimerMode
import com.dinzio.zendo.features.timer_settings.domain.usecase.GetBreakTimeUseCase
import com.dinzio.zendo.features.timer_settings.domain.usecase.GetFocusTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class GlobalTaskViewModel @Inject constructor(
    private val application: Application,
    private val globalTimerStateHolder: GlobalTimerStateHolder,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getFocusTimeUseCase: GetFocusTimeUseCase,
    private val getBreakTimeUseCase: GetBreakTimeUseCase
) : ViewModel() {

    val isTaskFinishedGlobal = TimerService.timerState.map { it.isFinished }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

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

    init {
        observeTimerFinish()
    }

    private fun observeTimerFinish() {
        viewModelScope.launch {
            TimerService.timerState.collect { serviceState ->
                if (serviceState.isTimerEnded) {
                    handleTimerFinished(serviceState.currentTaskId)
                }
            }
        }
    }

    private suspend fun handleTimerFinished(taskId: Int?) {
        if (taskId == null) {
            // Quick Timer Logic
            val currentMode = globalTimerStateHolder.quickTimerMode.value
            val nextMode = if (currentMode == TimerMode.FOCUS) TimerMode.BREAK else TimerMode.FOCUS
            globalTimerStateHolder.quickTimerMode.value = nextMode
            
            val nextMinutes = if (nextMode == TimerMode.FOCUS) {
                getFocusTimeUseCase().first()
            } else {
                getBreakTimeUseCase().first()
            }
            TimerService.sendAction(
                context = application,
                action = TimerService.ACTION_START,
                duration = (nextMinutes * 60).toLong(),
                taskId = null
            )
        } else {
            // Pomodoro Task Logic
            val task = getTaskByIdUseCase(taskId) ?: return
            
            if (task.lastMode == "FOCUS") {
                val updatedTask = task.copy(
                    lastMode = "BREAK",
                    lastSecondsLeft = 0L
                )
                updateTaskUseCase(updatedTask)
                
                TimerService.sendAction(
                    context = application,
                    action = TimerService.ACTION_START,
                    duration = (updatedTask.breakTime * 60).toLong(),
                    taskId = taskId,
                    taskName = updatedTask.title
                )
            } else {
                val newSessionDone = task.sessionDone + 1
                val isAllDone = newSessionDone >= task.sessionCount
                
                val updatedTask = task.copy(
                    sessionDone = newSessionDone,
                    isCompleted = isAllDone,
                    lastMode = "FOCUS",
                    lastSecondsLeft = 0L
                )
                updateTaskUseCase(updatedTask)
                
                if (isAllDone) {
                    // Semua selesai -> Celebration
                    TimerService.sendAction(application, TimerService.ACTION_FORCE_FINISHED)
                } else {
                    // Lanjut ke sesi Focus berikutnya
                    TimerService.sendAction(
                        context = application,
                        action = TimerService.ACTION_START,
                        duration = (updatedTask.focusTime * 60).toLong(),
                        taskId = taskId,
                        taskName = updatedTask.title
                    )
                }
            }
        }
    }

    fun completeTask(task: TaskModel?) {
        viewModelScope.launch {
            task?.let {
                val completedTask = it.copy(
                    isCompleted = true,
                    sessionDone = it.sessionCount,
                    lastSecondsLeft = 0L
                )
                updateTaskUseCase(completedTask)
            }
        }
    }
}

