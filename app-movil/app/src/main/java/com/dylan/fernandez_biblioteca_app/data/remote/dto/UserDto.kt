package com.dylan.fernandez_biblioteca_app.data.remote.dto

import com.dylan.fernandez_biblioteca_app.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name")  val lastName: String,
    @SerializedName("is_staff")   val isStaff: Boolean,
    @SerializedName("is_active")  val isActive: Boolean,
    @SerializedName("date_joined") val dateJoined: String,
)

fun UserDto.toDomain() = User(
    id = id,
    username = username,
    email = email,
    firstName = firstName,
    lastName = lastName,
    isStaff = isStaff,
    isActive = isActive,
    dateJoined = dateJoined
)