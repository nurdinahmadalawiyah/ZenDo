package com.dinzio.zendo.core.util

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import android.widget.Toast

/**
 * Mengecek apakah orientasi layar saat ini adalah Landscape.
 */
@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

/**
 * Helper simpel untuk menampilkan Toast (agar tidak perlu ketik Toast.makeText panjang-panjang).
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}