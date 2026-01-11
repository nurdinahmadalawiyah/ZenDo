package com.dinzio.zendo.features.category.domain.usecase

import com.dinzio.zendo.features.category.domain.model.CategoryModel
import com.dinzio.zendo.features.category.domain.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: CategoryModel) {
        if (category.name.isNotBlank()) {
            repository.insertCategory(category)
        }
    }
}