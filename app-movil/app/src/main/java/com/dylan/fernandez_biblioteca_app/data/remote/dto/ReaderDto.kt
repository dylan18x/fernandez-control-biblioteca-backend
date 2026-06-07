package com.dylan.fernandez_biblioteca_app.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.dylan.fernandez_biblioteca_app.domain.model.Reader
import com.dylan.fernandez_biblioteca_app.domain.model.ReaderPayload

data class ReaderDto(
    val id: Int,
    @SerializedName("nombre_completo") val fullName: String,
    @SerializedName("telefono") val phoneNumber: String,
    @SerializedName("correo") val email: String,
    @SerializedName("fecha_registro") val registrationDate: String
)

data class ReaderRequestDto(
    @SerializedName("nombre_completo") val fullName: String,
    @SerializedName("telefono") val phoneNumber: String,
    @SerializedName("correo") val email: String
)

fun ReaderDto.toDomain() = Reader(
    id               = id,
    fullName         = fullName,
    phoneNumber      = phoneNumber,
    email            = email,
    registrationDate = registrationDate
)

fun ReaderPayload.toRequest() = ReaderRequestDto(
    fullName    = fullName,
    phoneNumber = phoneNumber,
    email       = email
)