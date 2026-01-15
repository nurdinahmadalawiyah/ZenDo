package com.dinzio.zendo.features.category.domain.usecase

import com.dinzio.zendo.features.category.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryDetailUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(id: Int) = repository.getCategoryDetail(id)
}