package com.dylan.fernandez_biblioteca_app.domain.model

data class Book(
    val id: Int,
    val categoriaId: Int,
    val titulo: String,
    val autor: String,
    val editorial: String,
    val anioPublicacion: Int,
    val disponible: Boolean,
)

data class BookPayload(
    val categoriaId: Int,
    val titulo: String,
    val autor: String,
    val editorial: String,
    val anioPublicacion: Int,
    val disponible: Boolean = true,
)