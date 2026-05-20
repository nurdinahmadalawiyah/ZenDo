package com.dinzio.zendo.features.auth.domain.usecase

import com.dinzio.zendo.features.auth.domain.model.UserModel
import com.dinzio.zendo.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<UserModel?> {
        return repository.currentUser
    }
}