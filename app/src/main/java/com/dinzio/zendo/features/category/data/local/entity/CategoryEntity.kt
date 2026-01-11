package com.dinzio.zendo.features.category.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String,
    val createdAt: String,
    val updatedAt: String
)