package com.dylan.fernandez_biblioteca_app.domain.repository

import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBook
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBookPayload

interface CategoryBookRepositoryImp {
    suspend fun getCategories(search: String? = null): Result<List<CategoryBook>>
    suspend fun getCategory(id: Int): Result<CategoryBook>
    suspend fun createCategory(payload: CategoryBookPayload): Result<CategoryBook>
    suspend fun updateCategory(id: Int, payload: CategoryBookPayload): Result<CategoryBook>
    suspend fun deleteCategory(id: Int): Result<Unit>
}