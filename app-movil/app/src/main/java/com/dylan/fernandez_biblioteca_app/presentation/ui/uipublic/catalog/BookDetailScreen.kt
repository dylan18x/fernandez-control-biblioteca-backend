package com.dylan.fernandez_biblioteca_app.presentation.ui.uipublic.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.BookDetailViewModel
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.LoanCartViewModel
import com.dylan.fernandez_biblioteca_app.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookId: Int,
    onBack: () -> Unit,
    viewModel: BookDetailViewModel = hiltViewModel(),
    cartViewModel: LoanCartViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val cartBooks by cartViewModel.books.collectAsState()
    val isInCart = cartBooks.any { it.id == bookId }

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Libro") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            state.book?.let { book ->
                if (book.isAvailable) {
                    Surface(
                        tonalElevation = 8.dp,
                        shadowElevation = 8.dp,
                        color = Surface
                    ) {
                        Button(
                            onClick = {
                                if (isInCart) cartViewModel.removeBook(book.id)
                                else cartViewModel.addBook(book)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isInCart) Color.Gray else Accent,
                                contentColor = AccentOnDark
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(
                                if (isInCart) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                if (isInCart) "Quitar de la selección" else "Seleccionar para préstamo",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        when {
            state.isLoading -> LoadingScreen("Cargando información del libro...")
            state.error != null -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("❌ ${state.error}", color = Error)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { viewModel.loadBook(bookId) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            state.book != null -> {
                val book = state.book!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Background)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Hero Section with Emoji
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(Surface2),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📖", fontSize = 100.sp)
                        
                        if (!book.isAvailable) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(Error.copy(alpha = 0.8f))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "NO DISPONIBLE PARA PRÉSTAMO",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = book.categoryName ?: "Sin categoría",
                            style = MaterialTheme.typography.labelLarge,
                            color = Accent,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "por ${book.author}",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )

                        Spacer(Modifier.height(24.dp))
                        
                        Divider(color = Border)
                        
                        Spacer(Modifier.height(24.dp))

                        DetailRow("Editorial", book.publisher)
                        DetailRow("Año de publicación", book.publicationYear.toString())
                        DetailRow("ISBN", book.isbn)
                        DetailRow("Ubicación", book.location ?: "Estantería General")
                        
                        Spacer(Modifier.height(24.dp))
                        
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = book.description ?: "No hay descripción disponible para este libro.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary,
                            lineHeight = 24.sp
                        )
                        
                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextFaint, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, color = TextPrimary, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
    }
}
