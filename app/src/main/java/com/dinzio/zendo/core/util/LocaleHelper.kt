@file:Suppress("DEPRECATION")

package com.dinzio.zendo.core.util

import android.content.Context
import android.content.ContextWrapper
import java.util.Locale

fun wrapContext(context: Context, languageCode: String): ContextWrapper {
    val locale = if (languageCode == "system") {
        Locale.getDefault()
    } else {
        Locale(languageCode)
    }

    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)

    val newContext = context.createConfigurationContext(config)
    return ContextWrapper(newContext)
}