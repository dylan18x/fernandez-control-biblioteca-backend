package com.dylan.fernandez_biblioteca_app.presentation.ui.uipublic.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dylan.fernandez_biblioteca_app.presentation.components.LoadingScreen
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.LoanListViewModel
import com.dylan.fernandez_biblioteca_app.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanDetailScreen(
    loanId: Int,
    onBack: () -> Unit,
    viewModel: LoanListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val loan = state.loans.find { it.id == loanId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Préstamo #$loanId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = Background
    ) { padding ->
        if (loan == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                if (state.isLoading) {
                    LoadingScreen("Buscando préstamo...")
                } else {
                    Text("No se encontró el préstamo", color = TextPrimary)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Info Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, null, tint = Accent)
                                Spacer(Modifier.width(8.dp))
                                Text("Estado: ${loan.status}", fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Fecha solicitado: ${loan.loanDate}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, null, tint = Error, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Fecha devolución: ${loan.returnDate}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            }
                        }
                    }
                }

                item {
                    Text(
                        "Libros incluidos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(loan.details) { detail ->
                    Surface(
                        color = Surface,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Surface2, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📖")
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    detail.bookTitle,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    "ID Libro: ${detail.libroId}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
