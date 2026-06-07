package com.dylan.fernandez_biblioteca_app.presentation.ui.admin.loans

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dylan.fernandez_biblioteca_app.domain.model.Loan
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.LoanAdminViewModel

@Composable
fun LoanAdminScreen(
    onViewDetail: (Int) -> Unit,
    viewModel: LoanAdminViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error, state.successMessage) {
        state.error?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessages() }
        state.successMessage?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessages() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (state.isLoading && state.loans.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.loans) { loan ->
                        LoanAdminItem(
                            loan = loan,
                            onViewDetail = { onViewDetail(loan.id) },
                            onDelete = { viewModel.deleteLoan(loan.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoanAdminItem(
    loan: Loan,
    onViewDetail: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Préstamo #${loan.id}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Lector ID: ${loan.readerId}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Fecha: ${loan.loanDate}", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = loan.status.uppercase(),
                    color = when(loan.status) {
                        "active", "activo" -> Color(0xFF4CAF50)
                        "returned", "devuelto" -> Color.Gray
                        else -> Color.Red
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row {
                IconButton(onClick = onViewDetail) {
                    Icon(Icons.Default.Info, contentDescription = "Detalle", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
