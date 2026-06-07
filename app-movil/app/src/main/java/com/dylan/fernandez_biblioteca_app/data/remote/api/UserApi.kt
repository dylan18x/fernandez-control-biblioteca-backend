package com.dylan.fernandez_biblioteca_app.data.remote.api

import com.dylan.fernandez_biblioteca_app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("users/")
    suspend fun getUsers(
        @Query("search")   search:   String?,
        @Query("is_staff") isStaff:  Boolean?,
        @Query("is_active") isActive: Boolean?,
        @Query("page")     page:     Int?,
    ): Response<PaginatedDto<UserDto>>

    @GET("users/{id}/")
    suspend fun getUser(@Path("id") id: Int): Response<UserDto>

    @DELETE("users/{id}/")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>

    @POST("users/{id}/toggle-active/")
    suspend fun toggleActive(@Path("id") id: Int): Response<UserDto>

    @GET("users/stats/")
    suspend fun getStats(): Response<UserStatsDto>
}

data class UserStatsDto(
    val total: Int,
    val active: Int,
    val inactive: Int,
    val staff: Int,
)