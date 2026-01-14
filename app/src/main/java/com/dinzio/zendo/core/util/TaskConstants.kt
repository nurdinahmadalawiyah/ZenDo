package com.dinzio.zendo.core.util

import com.dinzio.zendo.R

object TaskConstants {
    val FOCUS_OPTIONS_RES = listOf(
        R.string._15_minutes,
        R.string._25_minutes,
        R.string._45_minutes,
        R.string._60_minutes,
        R.string._90_minutes
    )

    val FOCUS_OPTIONS = listOf(
        R.string._15_minutes to 15,
        R.string._25_minutes to 25,
        R.string._45_minutes to 45,
        R.string._60_minutes to 60,
        R.string._90_minutes to 90
    )

    val BREAK_OPTIONS_RES = listOf(
        R.string._5_minutes,
        R.string._10_minutes,
        R.string._15_minutes,
        R.string._20_minutes,
        R.string._30_minutes
    )

    val BREAK_OPTIONS = listOf(
        R.string._5_minutes to 5,
        R.string._10_minutes to 10,
        R.string._15_minutes to 15,
        R.string._20_minutes to 20,
        R.string._30_minutes to 30
    )
}