package com.dinzio.zendo.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.dinzio.zendo.MainActivity
import com.dinzio.zendo.R
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
    private val channelId = "timer_channel"
    private val notificationId = 101

    private var currentTaskName: String? = null

    companion object {
        private val _timerState = MutableStateFlow(TimerState())
        val timerState = _timerState.asStateFlow()

        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"

        const val EXTRA_TASK_NAME = "EXTRA_TASK_NAME"
        const val EXTRA_DURATION = "EXTRA_DURATION"
        const val EXTRA_TASK_ID = "EXTRA_TASK_ID"

        fun sendAction(context: Context, action: String, duration: Long? = null, taskId: Int? = null, taskName: String? = null) {
            val intent = Intent(context, TimerService::class.java).apply {
                this.action = action
                putExtra(EXTRA_DURATION, duration)
                putExtra(EXTRA_TASK_ID, taskId ?: -1)
                putExtra(EXTRA_TASK_NAME, taskName)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

        override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val duration = intent.getLongExtra(EXTRA_DURATION, 0L)
                val taskId = intent.getIntExtra(EXTRA_TASK_ID, -1).takeIf { it != -1 }
                currentTaskName = intent.getStringExtra(EXTRA_TASK_NAME)

                if (_timerState.value.currentTaskId != taskId) {
                    countDownTimer?.cancel()
                }

                val initialNotification = createNotification(getString(R.string.timer_starts))
                startForeground(notificationId, initialNotification)

                _timerState.update { it.copy(isRunning = true, currentTaskId = taskId) }
                startTimer(duration, taskId)
            }
            ACTION_PAUSE -> pauseTimer()
            ACTION_STOP -> stopTimer()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer(durationSeconds: Long, taskId: Int?) {
        countDownTimer?.cancel()

        _timerState.update { it.copy(
            secondsLeft = durationSeconds,
            isRunning = true,
            isFinished = false,
            currentTaskId = taskId
        ) }

        countDownTimer = object : CountDownTimer(durationSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                _timerState.update { it.copy(secondsLeft = seconds) }
                updateNotification("${seconds / 60}:${String.format("%02d", seconds % 60)}")
            }

            override fun onFinish() {
                _timerState.update { it.copy(
                    secondsLeft = 0,
                    isRunning = false,
                    isFinished = true
                ) }
                updateNotification(getString(R.string.session_over_time_to_rest_focus))
            }
        }.start()
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        _timerState.update { it.copy(isRunning = false) }
        updateNotification(getString(R.string.paused))
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        _timerState.update { TimerState() }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotification(content: String): Notification {
        val title = currentTaskName ?: "Quick Timer"

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setSilent(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()
    }

    private fun updateNotification(content: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, createNotification(content))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (manager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId,
                    "Timer ZenDo",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    setShowBadge(false)
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }
}