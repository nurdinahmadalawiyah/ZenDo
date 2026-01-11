package com.dinzio.zendo.features.category.domain.repository

import com.dinzio.zendo.features.category.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<CategoryModel>>
    suspend fun insertCategory(category: CategoryModel)
    suspend fun updateCategory(category: CategoryModel)
    suspend fun deleteCategory(category: CategoryModel)
}