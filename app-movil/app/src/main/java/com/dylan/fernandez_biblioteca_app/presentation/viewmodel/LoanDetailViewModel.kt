package com.dylan.fernandez_biblioteca_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylan.fernandez_biblioteca_app.domain.model.Loan
import com.dylan.fernandez_biblioteca_app.domain.repository.LoanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoanDetailUiState {
    data object Loading                    : LoanDetailUiState
    data class  Success(val loan: Loan)    : LoanDetailUiState
    data class  Error(val message: String) : LoanDetailUiState
}

@HiltViewModel
class LoanDetailViewModel @Inject constructor(
    private val repository: LoanRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<LoanDetailUiState>(LoanDetailUiState.Loading)
    val state: StateFlow<LoanDetailUiState> = _state.asStateFlow()

    fun load(id: Int) {
        viewModelScope.launch {
            _state.value = LoanDetailUiState.Loading
            repository.getLoan(id)
                .onSuccess { _state.value = LoanDetailUiState.Success(it) }
                .onFailure { _state.value = LoanDetailUiState.Error(it.message ?: "Error al cargar el detalle del préstamo") }
        }
    }
}
