package com.dinzio.zendo.features.category.data.local.entity

import androidx.room.Embedded

data class CategoryWithTaskCount(
    @Embedded val category: CategoryEntity,
    val taskCount: Int
)