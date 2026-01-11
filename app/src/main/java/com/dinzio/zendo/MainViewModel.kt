package com.dinzio.zendo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.core.data.local.LanguageManager
import com.dinzio.zendo.core.data.local.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeManager: ThemeManager,
    private val languageManager: LanguageManager
) : ViewModel() {

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
}