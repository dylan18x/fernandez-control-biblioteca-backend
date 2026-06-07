package com.dylan.fernandez_biblioteca_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylan.fernandez_biblioteca_app.domain.model.Book
import com.dylan.fernandez_biblioteca_app.domain.model.BookFilters
import com.dylan.fernandez_biblioteca_app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardStats(
    val totalActiveBooks:      Int    = 0,
    val outOfStockBooks:       Int    = 0,
    val totalBooks:            Int    = 0,
    val activeCategories:      Int    = 0,
    val totalCategories:       Int    = 0,
    val totalLoans:            Int    = 0,
    val pendingLoans:          Int    = 0,
    val loansByStatus:         Map<String, Int> = emptyMap(),
    val activeUsers:           Int    = 0,
    val totalUsers:            Int    = 0,
    val staffUsers:            Int    = 0,
    val lowStockBooks:         List<Book> = emptyList(),
)

sealed interface DashboardUiState {
    data object Loading                          : DashboardUiState
    data class  Success(val stats: DashboardStats) : DashboardUiState
    data class  Error(val message: String)       : DashboardUiState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bookRepository:     BookRepository,
    private val categoryRepository: CategoryBookRepository,
    private val loanRepository:     LoanRepository,
    private val userRepository:     UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    private val _lastUpdated = MutableStateFlow<Long>(0L)
    val lastUpdated: StateFlow<Long> = _lastUpdated.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = DashboardUiState.Loading

            try {
                // Todas las llamadas en paralelo con async
                val bookStatsDeferred     = async { bookRepository.getStats() }
                val categoryStatsDeferred = async { categoryRepository.getCategories() } // Simplificado para biblioteca
                val loanStatsDeferred     = async { loanRepository.getStats() }
                val userStatsDeferred     = async { userRepository.getStats() }
                val recentBooksDeferred   = async {
                    bookRepository.getBooks(BookFilters(isAvailable = true, page = 1))
                }

                val bookStats     = bookStatsDeferred.await().getOrNull() ?: emptyMap()
                val categories    = categoryStatsDeferred.await().getOrNull() ?: emptyList()
                val loanStats     = loanStatsDeferred.await().getOrNull() ?: emptyMap()
                val userStats     = userStatsDeferred.await().getOrNull() ?: emptyMap()
                val recentBooks   = recentBooksDeferred.await().getOrNull()

                @Suppress("UNCHECKED_CAST")
                val loansByStatus = (loanStats["by_status"] as? Map<String, Int>) ?: emptyMap()

                val stats = DashboardStats(
                    totalActiveBooks     = (bookStats["total_active"]   as? Int)    ?: 0,
                    outOfStockBooks      = (bookStats["out_of_stock"]   as? Int)    ?: 0,
                    totalBooks           = (bookStats["total_books"]    as? Int)    ?: 0,
                    activeCategories     = categories.size,
                    totalCategories      = categories.size,
                    totalLoans           = (loanStats["total_loans"]    as? Int)    ?: 0,
                    pendingLoans         = loansByStatus["PENDIENTE"]               ?: 0,
                    loansByStatus        = loansByStatus,
                    activeUsers          = userStats["active"]            ?: 0,
                    totalUsers           = userStats["total"]             ?: 0,
                    staffUsers           = userStats["staff"]             ?: 0,
                    lowStockBooks        = recentBooks?.first?.take(5) ?: emptyList(),
                )

                _state.value       = DashboardUiState.Success(stats)
                _lastUpdated.value = System.currentTimeMillis()

            } catch (e: Exception) {
                _state.value = DashboardUiState.Error(e.message ?: "Error al cargar el dashboard")
            }
        }
    }
}