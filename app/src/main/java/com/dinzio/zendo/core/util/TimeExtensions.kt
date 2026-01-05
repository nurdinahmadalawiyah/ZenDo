package com.dinzio.zendo.core.util

import java.util.Locale

/**
 * Mengubah Milliseconds (Long) menjadi format string waktu "MM:SS".
 * Contoh: 150000L -> "02:30"
 */
fun Long.toTimerString(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds)
}

/**
 * Mengubah Milliseconds menjadi format "HH:MM:SS" jika durasi lebih panjang (opsional).
 */
fun Long.toFullTimerString(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}