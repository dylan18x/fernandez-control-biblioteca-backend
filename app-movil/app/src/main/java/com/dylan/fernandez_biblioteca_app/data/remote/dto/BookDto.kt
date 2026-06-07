package com.dylan.fernandez_biblioteca_app.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.dylan.fernandez_biblioteca_app.domain.model.Book
import com.dylan.fernandez_biblioteca_app.domain.model.BookPayload

data class BookDto(
    val id: Int,
    @SerializedName("categoria_id") val categoryId: Int,
    @SerializedName("titulo") val title: String,
    @SerializedName("autor") val author: String,
    @SerializedName("editorial") val publisher: String,
    @SerializedName("anio_publicacion") val publicationYear: Int,
    @SerializedName("disponible") val isAvailable: Boolean
)

data class BookRequestDto(
    @SerializedName("categoria_id") val categoryId: Int,
    @SerializedName("titulo") val title: String,
    @SerializedName("autor") val author: String,
    @SerializedName("editorial") val publisher: String,
    @SerializedName("anio_publicacion") val publicationYear: Int,
    @SerializedName("disponible") val isAvailable: Boolean
)

fun BookDto.toDomain() = Book(
    id              = id,
    categoryId      = categoryId,
    title           = title,
    author          = author,
    publisher       = publisher,
    publicationYear = publicationYear,
    isAvailable     = isAvailable
)

fun BookPayload.toRequest() = BookRequestDto(
    categoryId      = categoryId,
    title           = title,
    author          = author,
    publisher       = publisher,
    publicationYear = publicationYear,
    isAvailable     = isAvailable
)