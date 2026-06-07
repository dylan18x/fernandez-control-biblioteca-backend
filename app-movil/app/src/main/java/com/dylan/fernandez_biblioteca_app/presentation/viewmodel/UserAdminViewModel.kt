package com.dylan.fernandez_biblioteca_app.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylan.fernandez_biblioteca_app.domain.model.User
import com.dylan.fernandez_biblioteca_app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserAdminState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class UserAdminViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(UserAdminState())
    val state: State<UserAdminState> = _state

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            repository.getUsers()
                .onSuccess { (users, _) ->
                    _state.value = _state.value.copy(users = users, isLoading = false)
                }
                .onFailure {
                    _state.value = _state.value.copy(isLoading = false, error = "Error al cargar usuarios")
                }
        }
    }

    fun toggleUserActive(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.toggleActive(id)
                .onSuccess {
                    _state.value = _state.value.copy(
                        successMessage = "Estado de usuario actualizado",
                        isLoading = false
                    )
                    loadUsers()
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "No se pudo actualizar el estado"
                    )
                }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.deleteUser(id)
                .onSuccess {
                    _state.value = _state.value.copy(
                        successMessage = "Usuario eliminado",
                        isLoading = false
                    )
                    loadUsers()
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "No se pudo eliminar el usuario"
                    )
                }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(error = null, successMessage = null)
    }
}
