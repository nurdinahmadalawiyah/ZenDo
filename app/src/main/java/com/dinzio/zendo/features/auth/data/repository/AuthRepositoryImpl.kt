package com.dinzio.zendo.features.auth.data.repository

import com.dinzio.zendo.features.auth.data.mapper.toDomain
import com.dinzio.zendo.features.auth.domain.model.UserModel
import com.dinzio.zendo.features.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: Flow<UserModel?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomain())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun signInAnonymously(): Result<UserModel> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            val user = result.user ?: throw Exception("Failed to sign in anonymously")
            Result.success(user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun linkAnonymousWithGoogle(idToken: String): Result<UserModel> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val userInstance = firebaseAuth.currentUser

            val result = if (userInstance != null && userInstance.isAnonymous) {
                userInstance.linkWithCredential(credential).await()
            } else {
                firebaseAuth.signInWithCredential(credential).await()
            }
            
            val updatedUser = result.user ?: throw Exception("Failed to authenticate with Google")
            Result.success(updatedUser.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}