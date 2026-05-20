package com.dinzio.zendo.features.auth.domain.usecase

import com.dinzio.zendo.features.auth.domain.model.UserModel
import com.dinzio.zendo.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LinkGoogleAccountUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<UserModel> {
        return repository.linkAnonymousWithGoogle(idToken)
    }
}