package com.dinzio.zendo.features.category.data.repository

import com.dinzio.zendo.features.category.data.local.dao.CategoryDao
import com.dinzio.zendo.features.category.data.mapper.toDomain
import com.dinzio.zendo.features.category.data.mapper.toEntity
import com.dinzio.zendo.features.category.domain.model.CategoryDetailModel
import com.dinzio.zendo.features.category.domain.model.CategoryModel
import com.dinzio.zendo.features.category.domain.repository.CategoryRepository
import com.dinzio.zendo.features.task.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getCategories(): Flow<List<CategoryModel>> = categoryDao.getAllCategories().map { entities ->
        entities.map { it.toDomain() }
    }

    override fun getCategoryDetail(id: Int): Flow<CategoryDetailModel?> {
        return categoryDao.getCategoryDetailWithStats(id).map { entity ->
            entity?.let {
                CategoryDetailModel(
                    category = it.category.toDomain(),
                    totalTasks = it.totalTasks,
                    completedTasks = it.completedTasks,
                    pendingTasks = it.pendingTasks,
                    tasks = it.tasks.map { taskEntity -> taskEntity.toDomain() }
                )
            }
        }
    }

    override suspend fun getCategoryById(id: Int): CategoryModel? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }

    override suspend fun insertCategory(category: CategoryModel) {
        categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun updateCategory(category: CategoryModel) {
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: CategoryModel) {
        categoryDao.deleteCategory(category.toEntity())
    }
}