package com.dinzio.zendo.core.util

fun String.extractMinutes(): Int = this.filter { it.isDigit() }.toIntOrNull() ?: 0