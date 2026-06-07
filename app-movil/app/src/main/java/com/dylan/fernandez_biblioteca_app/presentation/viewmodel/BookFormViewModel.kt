package com.dylan.fernandez_biblioteca_app.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylan.fernandez_biblioteca_app.domain.model.BookPayload
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBook
import com.dylan.fernandez_biblioteca_app.domain.repository.BookRepository
import com.dylan.fernandez_biblioteca_app.domain.repository.CategoryBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookFormState(
    val title: String = "",
    val author: String = "",
    val publisher: String = "",
    val publicationYear: String = "",
    val categoryId: Int? = null,
    val isAvailable: Boolean = true,
    val categories: List<CategoryBook> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BookFormViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val categoryRepository: CategoryBookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(BookFormState())
    val state: State<BookFormState> = _state

    private val bookId: Int? = savedStateHandle.get<String>("bookId")?.toIntOrNull()

    init {
        loadCategories()
        bookId?.let { loadBook(it) }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories().onSuccess { cats ->
                _state.value = _state.value.copy(categories = cats)
            }
        }
    }

    private fun loadBook(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            bookRepository.getBook(id).onSuccess { book ->
                _state.value = _state.value.copy(
                    title = book.title,
                    author = book.author,
                    publisher = book.publisher,
                    publicationYear = book.publicationYear.toString(),
                    categoryId = book.categoryId,
                    isAvailable = book.isAvailable,
                    isLoading = false
                )
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, error = "Error al cargar el libro")
            }
        }
    }

    fun onTitleChange(value: String) { _state.value = _state.value.copy(title = value) }
    fun onAuthorChange(value: String) { _state.value = _state.value.copy(author = value) }
    fun onPublisherChange(value: String) { _state.value = _state.value.copy(publisher = value) }
    fun onYearChange(value: String) { _state.value = _state.value.copy(publicationYear = value) }
    fun onCategoryChange(id: Int) { _state.value = _state.value.copy(categoryId = id) }
    fun onAvailabilityChange(value: Boolean) { _state.value = _state.value.copy(isAvailable = value) }

    fun saveBook() {
        val currentState = _state.value
        if (currentState.title.isBlank() || currentState.author.isBlank() || currentState.categoryId == null) {
            _state.value = _state.value.copy(error = "Completa los campos obligatorios")
            return
        }

        val payload = BookPayload(
            title = currentState.title,
            author = currentState.author,
            publisher = currentState.publisher,
            publicationYear = currentState.publicationYear.toIntOrNull() ?: 2024,
            categoryId = currentState.categoryId,
            isAvailable = currentState.isAvailable
        )

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = if (bookId == null) {
                bookRepository.createBook(payload)
            } else {
                bookRepository.updateBook(bookId, payload)
            }

            if (result.isSuccess) {
                _state.value = _state.value.copy(isSuccess = true, isLoading = false)
            } else {
                _state.value = _state.value.copy(isLoading = false, error = "Error al guardar el libro")
            }
        }
    }
}
