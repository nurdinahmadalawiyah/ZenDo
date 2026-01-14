package com.dinzio.zendo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.core.data.local.BreakTimerManager
import com.dinzio.zendo.core.data.local.FocusTimerManager
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
    private val languageManager: LanguageManager,
    private val focusTimerManager: FocusTimerManager,
    private val breakTimerManager: BreakTimerManager,
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

    val focusTime: StateFlow<Int> = focusTimerManager.focusTime.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 25
        )

    fun setFocusTime(time: Int) {
        viewModelScope.launch {
            focusTimerManager.setFocusTime(time)
        }
    }

    val breakTime: StateFlow<Int> = breakTimerManager.breakTime.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 5
        )

    fun setBreakTime(time: Int) {
        viewModelScope.launch {
            breakTimerManager.setBreakTime(time)
        }
    }
}