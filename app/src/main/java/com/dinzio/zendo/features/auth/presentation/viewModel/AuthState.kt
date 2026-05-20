package com.dinzio.zendo.features.auth.presentation.viewModel

import com.dinzio.zendo.features.auth.domain.model.UserModel

data class AuthState(
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)