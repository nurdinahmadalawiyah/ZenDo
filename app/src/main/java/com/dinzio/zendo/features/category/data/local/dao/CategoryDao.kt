package com.dinzio.zendo.features.category.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dinzio.zendo.features.category.data.local.entity.CategoryEntity
import com.dinzio.zendo.features.category.data.local.entity.CategoryWithTaskCount
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("""
        SELECT categories.*, COUNT(tasks.id) as taskCount 
        FROM categories 
        LEFT JOIN tasks ON categories.id = tasks.category_id 
        GROUP BY categories.id 
        ORDER BY categories.createdAt DESC
    """)
    fun getAllCategories(): Flow<List<CategoryWithTaskCount>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
}