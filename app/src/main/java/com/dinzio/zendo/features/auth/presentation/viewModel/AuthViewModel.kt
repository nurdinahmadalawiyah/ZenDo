package com.dinzio.zendo.features.auth.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinzio.zendo.features.auth.domain.usecase.LinkGoogleAccountUseCase
import com.dinzio.zendo.features.auth.domain.usecase.ObserveUserUseCase
import com.dinzio.zendo.features.auth.domain.usecase.SignInAnonymouslyUseCase
import com.dinzio.zendo.features.auth.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val observeUserUseCase: ObserveUserUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val linkGoogleAccountUseCase: LinkGoogleAccountUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            observeUserUseCase().collect { user ->
                _state.update { it.copy(user = user) }
                if (user == null) {
                    onEvent(AuthEvent.OnSignInAnonymously)
                }
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnSignInAnonymously -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true, error = null) }
                    signInAnonymouslyUseCase()
                        .onSuccess { user -> _state.update { it.copy(user = user, isLoading = false) } }
                        .onFailure { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
                }
            }
            is AuthEvent.OnLinkGoogleAccount -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true, error = null) }
                    linkGoogleAccountUseCase(event.idToken)
                        .onSuccess { user -> _state.update { it.copy(user = user, isLoading = false) } }
                        .onFailure { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
                }
            }
            is AuthEvent.OnSignOut -> {
                viewModelScope.launch {
                    signOutUseCase()
                }
            }
        }
    }
}