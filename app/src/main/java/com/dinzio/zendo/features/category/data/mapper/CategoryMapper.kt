package com.dinzio.zendo.features.category.data.mapper

import com.dinzio.zendo.features.category.data.local.entity.CategoryEntity
import com.dinzio.zendo.features.category.data.local.entity.CategoryWithTaskCount
import com.dinzio.zendo.features.category.domain.model.CategoryModel

fun CategoryWithTaskCount.toDomain() = CategoryModel(
    id = category.id,
    name = category.name,
    icon = category.icon,
    taskCount = taskCount,
    createdAt = category.createdAt,
    updatedAt = category.updatedAt
)

fun CategoryEntity.toDomain(taskCount: Int = 0) = CategoryModel(
    id = id,
    name = name,
    icon = icon,
    taskCount = taskCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CategoryModel.toEntity() = CategoryEntity(
    id = id,
    name = name,
    icon = icon,
    createdAt = createdAt,
    updatedAt = updatedAt
)