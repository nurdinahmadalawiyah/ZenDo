package com.dinzio.zendo.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TimerState(
    val secondsLeft: Long = 0,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val currentTaskId: Int? = null
)

class TimerService : LifecycleService() {

    private var countDownTimer: CountDownTimer? = null

    companion object {
        private val _timerState = MutableStateFlow(TimerState())
        val timerState = _timerState.asStateFlow()

        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"

        const val EXTRA_DURATION = "EXTRA_DURATION"
        const val EXTRA_TASK_ID = "EXTRA_TASK_ID"

        fun sendAction(context: Context, action: String, duration: Long? = null, taskId: Int? = null) {
            val intent = Intent(context, TimerService::class.java).apply {
                this.action = action
                duration?.let { putExtra(EXTRA_DURATION, it) }
                taskId?.let { putExtra(EXTRA_TASK_ID, it) }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val initialNotification = createNotification("Menyiapkan timer...")
        startForeground(101, initialNotification)

        when (intent?.action) {
            ACTION_START -> {
                val duration = intent.getLongExtra(EXTRA_DURATION, 0L)
                val taskId = intent.getIntExtra(EXTRA_TASK_ID, -1).takeIf { it != -1 }
                startTimer(duration, taskId)
            }
            ACTION_PAUSE -> pauseTimer()
            ACTION_STOP -> stopTimer()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer(durationSeconds: Long, taskId: Int?) {
        countDownTimer?.cancel()

        _timerState.update {
            it.copy(
                secondsLeft = durationSeconds,
                isRunning = true,
                isFinished = false,
                currentTaskId = taskId
            )
        }

        countDownTimer = object : CountDownTimer(durationSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                _timerState.update { it.copy(secondsLeft = seconds) }
                updateNotification("$seconds detik tersisa")
            }

            override fun onFinish() {
                _timerState.update { it.copy(isRunning = false, isFinished = true, secondsLeft = 0) }
                stopForeground(STOP_FOREGROUND_DETACH)
            }
        }.start()
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        _timerState.update { it.copy(isRunning = false) }
        updateNotification("Timer dijeda")
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        _timerState.update { TimerState() }
        stopSelf()
    }

    private fun createNotification(content: String): Notification {
        val channelId = "timer_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Timer ZenDo",
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("ZenDo Focus")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun updateNotification(content: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(101, createNotification(content))
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }
}