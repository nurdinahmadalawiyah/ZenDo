package com.dinzio.zendo.features.auth.domain.usecase

import com.dinzio.zendo.features.auth.domain.model.UserModel
import com.dinzio.zendo.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignInAnonymouslyUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserModel> {
        return repository.signInAnonymously()
    }
}