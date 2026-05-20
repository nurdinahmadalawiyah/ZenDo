package com.dinzio.zendo.features.auth.domain.repository

import com.dinzio.zendo.features.auth.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<UserModel?>
    suspend fun signInAnonymously(): Result<UserModel>
    suspend fun linkAnonymousWithGoogle(idToken: String): Result<UserModel>
    suspend fun signOut(): Result<Unit>
}