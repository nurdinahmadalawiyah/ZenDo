package com.dinzio.zendo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.dinzio.zendo.core.service.TimerService
import com.dinzio.zendo.features.language.domain.usecase.GetLanguageUseCase
import com.dinzio.zendo.features.language.domain.usecase.SetLanguageUseCase

import com.dinzio.zendo.features.theme.domain.usecase.GetThemeUseCase
import com.dinzio.zendo.features.theme.domain.usecase.SetThemeUseCase
import com.dinzio.zendo.features.timer_settings.domain.usecase.GetBreakTimeUseCase
import com.dinzio.zendo.features.timer_settings.domain.usecase.GetFocusTimeUseCase
import com.dinzio.zendo.features.timer_settings.domain.usecase.SetBreakTimeUseCase
import com.dinzio.zendo.features.timer_settings.domain.usecase.SetFocusTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val getFocusTimeUseCase: GetFocusTimeUseCase,
    private val setFocusTimeUseCase: SetFocusTimeUseCase,
    private val getBreakTimeUseCase: GetBreakTimeUseCase,
    private val setBreakTimeUseCase: SetBreakTimeUseCase
) : ViewModel() {


    val themeMode: StateFlow<String> = getThemeUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "system"
        )

    fun setTheme(mode: String) {
        viewModelScope.launch {
            setThemeUseCase(mode)
        }
    }

    val languageCode: StateFlow<String> = getLanguageUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "system"
        )

    fun setLanguage(code: String) {
        viewModelScope.launch {
            setLanguageUseCase(code)
        }
    }

    val focusTime: StateFlow<Int> = getFocusTimeUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 25
        )

    fun setFocusTime(time: Int) {
        viewModelScope.launch {
            setFocusTimeUseCase(time)
        }
    }

    val breakTime: StateFlow<Int> = getBreakTimeUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 5
        )

    fun setBreakTime(time: Int) {
        viewModelScope.launch {
            setBreakTimeUseCase(time)
        }
    }


}