package com.dinzio.zendo.features.category.domain.model

data class CategoryModel(
    val id: Int = 0,
    val name: String,
    val icon: String,
    val taskCount: Int,
    val createdAt: String,
    val updatedAt: String
)