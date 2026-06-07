package com.dylan.fernandez_biblioteca_app.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylan.fernandez_biblioteca_app.domain.model.Loan
import com.dylan.fernandez_biblioteca_app.domain.repository.LoanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoanAdminState(
    val loans: List<Loan> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class LoanAdminViewModel @Inject constructor(
    private val repository: LoanRepository
) : ViewModel() {

    private val _state = mutableStateOf(LoanAdminState())
    val state: State<LoanAdminState> = _state

    init {
        loadLoans()
    }

    fun loadLoans() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            repository.getLoans()
                .onSuccess { (loans, _) ->
                    _state.value = _state.value.copy(loans = loans, isLoading = false)
                }
                .onFailure {
                    _state.value = _state.value.copy(isLoading = false, error = "Error al cargar préstamos")
                }
        }
    }

    fun deleteLoan(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.deleteLoan(id)
                .onSuccess {
                    _state.value = _state.value.copy(
                        successMessage = "Préstamo eliminado",
                        isLoading = false
                    )
                    loadLoans()
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "No se pudo eliminar el préstamo"
                    )
                }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(error = null, successMessage = null)
    }
}
