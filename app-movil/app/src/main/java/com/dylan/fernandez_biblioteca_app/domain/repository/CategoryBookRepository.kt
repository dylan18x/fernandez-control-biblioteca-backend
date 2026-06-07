package com.dylan.fernandez_biblioteca_app.data.repository

import com.dylan.fernandez_biblioteca_app.data.remote.dto.toDomain
import com.dylan.fernandez_biblioteca_app.data.remote.dto.toRequest
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBook
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBookPayload
import com.dylan.fernandez_biblioteca_app.domain.repository.CategoryBookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryBookRepositoryImpl @Inject constructor(
    private val api: CategoryBookApi,
) : CategoryBookRepository {

    override suspend fun getCategories(search: String?): Result<List<CategoryBook>> = runCatching {
        val response = api.getCategories(search)
        if (response.isSuccessful) {
            response.body()!!.results.map { it.toDomain() }
        } else {
            error("Error ${response.code()}: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getCategory(id: Int): Result<CategoryBook> = runCatching {
        val response = api.getCategory(id)
        if (response.isSuccessful) response.body()!!.toDomain()
        else error("Error ${response.code()}")
    }

    override suspend fun createCategory(payload: CategoryBookPayload): Result<CategoryBook> = runCatching {
        val response = api.createCategory(payload.toRequest())
        if (response.isSuccessful) response.body()!!.toDomain()
        else error("Error ${response.code()}: ${response.errorBody()?.string()}")
    }

    override suspend fun updateCategory(id: Int, payload: CategoryBookPayload): Result<CategoryBook> = runCatching {
        val response = api.updateCategory(id, payload.toRequest())
        if (response.isSuccessful) response.body()!!.toDomain()
        else error("Error ${response.code()}: ${response.errorBody()?.string()}")
    }

    override suspend fun deleteCategory(id: Int): Result<Unit> = runCatching {
        val response = api.deleteCategory(id)
        if (!response.isSuccessful) error("Error ${response.code()}")
    }
}