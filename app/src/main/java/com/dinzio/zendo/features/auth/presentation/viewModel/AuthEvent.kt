package com.dinzio.zendo.features.auth.presentation.viewModel

sealed class AuthEvent {
    object OnSignInAnonymously : AuthEvent()
    data class OnLinkGoogleAccount(val idToken: String) : AuthEvent()
    object OnSignOut : AuthEvent()
}