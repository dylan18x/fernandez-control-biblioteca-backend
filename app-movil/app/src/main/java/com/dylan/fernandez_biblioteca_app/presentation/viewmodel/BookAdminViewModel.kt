package com.dylan.fernandez_biblioteca_app.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylan.fernandez_biblioteca_app.domain.model.*
import com.dylan.fernandez_biblioteca_app.domain.repository.BookRepository
import com.dylan.fernandez_biblioteca_app.domain.repository.CategoryBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookAdminState(
    val books: List<Book> = emptyList(),
    val categories: List<CategoryBook> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class BookAdminViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val categoryRepository: CategoryBookRepository
) : ViewModel() {

    private val _state = mutableStateOf(BookAdminState())
    val state: State<BookAdminState> = _state

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            val categoriesResult = categoryRepository.getCategories()
            val booksResult = bookRepository.getBooks(BookFilters())

            if (categoriesResult.isSuccess && booksResult.isSuccess) {
                _state.value = _state.value.copy(
                    categories = categoriesResult.getOrNull() ?: emptyList(),
                    books = booksResult.getOrNull()?.first ?: emptyList(),
                    isLoading = false
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar datos"
                )
            }
        }
    }

    fun deleteBook(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = bookRepository.deleteBook(id)
            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    books = _state.value.books.filter { it.id != id },
                    successMessage = "Libro eliminado correctamente",
                    isLoading = false
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "No se pudo eliminar el libro"
                )
            }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(error = null, successMessage = null)
    }
}
