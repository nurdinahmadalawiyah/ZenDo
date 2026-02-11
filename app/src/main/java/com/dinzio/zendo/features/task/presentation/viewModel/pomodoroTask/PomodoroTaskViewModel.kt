package com.dinzio.zendo.features.task.presentation.viewModel.pomodoroTask

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.core.service.TimerService
import com.dinzio.zendo.features.task.domain.model.TaskModel
import com.dinzio.zendo.features.task.domain.usecase.GetTaskByIdUseCase
import com.dinzio.zendo.features.task.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroTaskViewModel @Inject constructor(
    private val application: Application,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    private val _taskData = MutableStateFlow<TaskModel?>(null)
    private val _currentMode = MutableStateFlow(TimerMode.FOCUS)

    init {
        loadTaskData()
        observeTimerFinish()
        observeTimerTicks()
    }

    private fun loadTaskData() {
        viewModelScope.launch {
            val task = getTaskByIdUseCase(taskId)
            _taskData.value = task

            task?.let {
                val serviceState = TimerService.timerState.value
                val isServiceRunningThisTask = serviceState.isRunning && serviceState.currentTaskId == taskId

                if (isServiceRunningThisTask) {
                    _currentMode.value = if (it.lastMode == "BREAK") TimerMode.BREAK else TimerMode.FOCUS
                } else {
                    _currentMode.value = if (it.lastMode == "BREAK") TimerMode.BREAK else TimerMode.FOCUS
                }
            }
        }
    }

    private fun observeTimerFinish() {
        viewModelScope.launch {
            TimerService.timerState.collect { serviceState ->
                if (serviceState.isFinished && serviceState.currentTaskId == taskId) {
                    handleTimerFinished()
                }
            }
        }
    }

    private suspend fun handleTimerFinished() {
        val currentTask = _taskData.value ?: return
        val currentMode = _currentMode.value

        TimerService.sendAction(application, TimerService.ACTION_STOP)

        if (currentMode == TimerMode.FOCUS) {
            _currentMode.value = TimerMode.BREAK

            val updatedTask = currentTask.copy(
                lastMode = "BREAK",
                lastSecondsLeft = 0L
            )
            updateTaskUseCase(updatedTask)
            _taskData.value = updatedTask
        } else {
            val newSessionDone = currentTask.sessionDone + 1
            val isAllDone = newSessionDone >= currentTask.sessionCount

            val updatedTask = currentTask.copy(
                sessionDone = newSessionDone,
                isCompleted = isAllDone,
                lastMode = "FOCUS",
                lastSecondsLeft = 0L
            )

            updateTaskUseCase(updatedTask)
            _taskData.value = updatedTask

            if (isAllDone) {
                _currentMode.value = TimerMode.FOCUS
            } else {
                _currentMode.value = TimerMode.FOCUS
            }
        }
    }

    private fun observeTimerTicks() {
        viewModelScope.launch {
            TimerService.timerState.collect { serviceState ->
                if (serviceState.isRunning && serviceState.currentTaskId == taskId) {
                    val currentTask = _taskData.value
                    if (currentTask != null) {
                        updateTaskUseCase(
                            currentTask.copy(
                                lastSecondsLeft = serviceState.secondsLeft,
                                lastMode = if (_currentMode.value == TimerMode.BREAK) "BREAK" else "FOCUS"
                        )
                        )
                    }
                }
            }
        }
    }

    val state = combine(
        _taskData,
        _currentMode,
        TimerService.timerState,
    ) { task, mode, serviceState ->
        val isSameTask = serviceState.currentTaskId == taskId

        val totalSeconds = if (mode == TimerMode.FOCUS) {
            (task?.focusTime ?: 25) * 60L
        } else {
            (task?.breakTime ?: 5) * 60L
        }

        val displayTime = when {
            isSameTask && serviceState.secondsLeft > 0 -> {
                serviceState.secondsLeft
            }
            !isSameTask && (task?.lastSecondsLeft ?: 0L) > 0L -> {
                task!!.lastSecondsLeft
            }
            else -> totalSeconds
        }

        PomodoroTaskState(
            taskName = task?.title ?: "No Task",
            selectedMinutes = if (mode == TimerMode.FOCUS) (task?.focusTime ?: 25) else (task?.breakTime ?: 5),
            timerServiceState = serviceState,
            isSettingTimer = !(serviceState.isRunning && isSameTask),
            mode = mode,
            currentTime = displayTime,
            totalTime = totalSeconds,
            isRunning = serviceState.isRunning && isSameTask,
            currentSession = (task?.sessionDone ?: 0) + 1,
            totalSession = task?.sessionCount ?: 4
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PomodoroTaskState()
    )

    fun toggleTimer() {
        if (state.value.isRunning) pauseTimer() else startTimer()
    }

    fun startTimer() {
        val currentTask = _taskData.value
        val serviceState = TimerService.timerState.value
        val isSameTask = serviceState.currentTaskId == taskId

        val duration = when {
            isSameTask && serviceState.secondsLeft > 0 -> {
                serviceState.secondsLeft
            }
            (currentTask?.lastSecondsLeft ?: 0L) > 0L -> {
                currentTask!!.lastSecondsLeft
            }
            else -> {
                val minutes = if (_currentMode.value == TimerMode.FOCUS) {
                    currentTask?.focusTime ?: 25
                } else {
                    currentTask?.breakTime ?: 5
                }
                (minutes * 60).toLong()
            }
        }

        TimerService.sendAction(
            context = application,
            action = TimerService.ACTION_START,
            duration = duration,
            taskId = taskId,
            taskName = currentTask?.title
        )
    }

    fun pauseTimer() {
        TimerService.sendAction(application, TimerService.ACTION_PAUSE)
    }

    fun resetTimer() {
        val currentTask = _taskData.value

        viewModelScope.launch {
            TimerService.sendAction(application, TimerService.ACTION_STOP)
            currentTask?.let { it: TaskModel ->
                val updatedTask = it.copy(
                    lastSecondsLeft = 0L,
                    lastMode = "FOCUS"
                )
                updateTaskUseCase(updatedTask)
                _taskData.value = updatedTask
            }
            _currentMode.value = TimerMode.FOCUS
        }
    }

    fun skipPhase() {
        val currentMode = _currentMode.value
        val currentTask = _taskData.value ?: return

        viewModelScope.launch {
            if (currentMode == TimerMode.BREAK) {
                val newSessionDone = currentTask.sessionDone + 1
                val isAllDone = newSessionDone >= currentTask.sessionCount

                if (isAllDone) {
                    val updatedTask = currentTask.copy(
                        sessionDone = currentTask.sessionCount,
                        isCompleted = true,
                        lastSecondsLeft = 0L,
                        lastMode = "FOCUS"
                    )
                    updateTaskUseCase(updatedTask)

                    TimerService.sendAction(application, TimerService.ACTION_FORCE_FINISHED)
                } else {
                    val updatedTask = currentTask.copy(
                        sessionDone = newSessionDone,
                        lastSecondsLeft = 0L,
                        lastMode = "FOCUS"
                    )
                    updateTaskUseCase(updatedTask)
                    _taskData.value = updatedTask
                    TimerService.sendAction(application, TimerService.ACTION_STOP)
                    _currentMode.value = TimerMode.FOCUS
                }
            } else {
                _currentMode.value = TimerMode.BREAK

                val updatedTask = currentTask.copy(
                    lastSecondsLeft = 0L,
                    lastMode = "BREAK"
                )
                updateTaskUseCase(updatedTask)
                _taskData.value = updatedTask

                TimerService.sendAction(application, TimerService.ACTION_STOP)
                kotlinx.coroutines.delay(100)
            }
        }
    }
}