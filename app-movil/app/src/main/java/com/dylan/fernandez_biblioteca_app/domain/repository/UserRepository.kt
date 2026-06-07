package com.dylan.fernandez_biblioteca_app.domain.repository

import com.dylan.fernandez_biblioteca_app.domain.model.User

interface UserRepository {
    suspend fun getUsers(
        search:   String? = null,
        isStaff:  Boolean? = null,
        isActive: Boolean? = null,
        page:     Int? = null,
    ): Result<Pair<List<User>, Int>>

    suspend fun getUser(id: Int): Result<User>
    suspend fun deleteUser(id: Int): Result<Unit>
    suspend fun toggleActive(id: Int): Result<Boolean>
    suspend fun getStats(): Result<Map<String, Int>>
}