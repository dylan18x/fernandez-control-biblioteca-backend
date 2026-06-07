package com.dylan.fernandez_biblioteca_app.domain.model

data class CategoryBook(
    val id: Int,
    val nombre: String,
    val descripcion: String,
)

data class CategoryBookPayload(
    val nombre: String,
    val descripcion: String,
)