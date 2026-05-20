package com.dinzio.zendo.features.auth.domain.model

data class UserModel (
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val isAnonymous: Boolean
)