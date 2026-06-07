package com.dylan.fernandez_biblioteca_app.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBook
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBookPayload
import com.dylan.fernandez_biblioteca_app.domain.repository.CategoryBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryAdminState(
    val categories: List<CategoryBook> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class CategoryAdminViewModel @Inject constructor(
    private val repository: CategoryBookRepository
) : ViewModel() {

    private val _state = mutableStateOf(CategoryAdminState())
    val state: State<CategoryAdminState> = _state

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            repository.getCategories()
                .onSuccess { cats ->
                    _state.value = _state.value.copy(categories = cats, isLoading = false)
                }
                .onFailure {
                    _state.value = _state.value.copy(isLoading = false, error = "Error al cargar categorías")
                }
        }
    }

    fun saveCategory(id: Int?, name: String) {
        if (name.isBlank()) return
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val payload = CategoryBookPayload(name = name)
            val result = if (id == null) {
                repository.createCategory(payload)
            } else {
                repository.updateCategory(id, payload)
            }

            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    successMessage = "Categoría guardada",
                    isLoading = false
                )
                loadCategories()
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "No se pudo guardar la categoría"
                )
            }
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.deleteCategory(id)
                .onSuccess {
                    _state.value = _state.value.copy(
                        successMessage = "Categoría eliminada",
                        isLoading = false
                    )
                    loadCategories()
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "No se pudo eliminar la categoría"
                    )
                }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(error = null, successMessage = null)
    }
}
