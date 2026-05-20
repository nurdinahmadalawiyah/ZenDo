package com.dinzio.zendo.features.auth.data.mapper

import com.dinzio.zendo.features.auth.domain.model.UserModel
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): UserModel {
    return UserModel(
        uid = this.uid,
        email = this.email,
        displayName = this.displayName,
        photoUrl = this.photoUrl?.toString(),
        isAnonymous = this.isAnonymous
    )
}