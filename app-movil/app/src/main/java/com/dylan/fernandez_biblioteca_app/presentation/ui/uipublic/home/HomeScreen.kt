// presentation/ui/uipublic/home/HomeScreen.kt
package com.dylan.fernandez_biblioteca_app.presentation.ui.uipublic.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dylan.fernandez_biblioteca_app.domain.model.Book
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.CatalogViewModel
import com.dylan.fernandez_biblioteca_app.theme.*

@Composable
fun HomeScreen(
    onBookClick:    (Int) -> Unit,
    onCatalogClick: () -> Unit,
    viewModel:      CatalogViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier            = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding      = PaddingValues(bottom = 24.dp),
    ) {
        // ── Hero ──────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(Surface2, Background),
                        ),
                    )
                    .padding(horizontal = 24.dp, vertical = 48.dp),
            ) {
                Column {
                    Text(
                        text       = "Expande tu",
                        fontSize   = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color      = TextSecondary,
                    )
                    Text(
                        text       = "conocimiento",
                        fontSize   = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Accent,
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text  = "Explora el catálogo completo de nuestra biblioteca virtual.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = onCatalogClick,
                        colors  = ButtonDefaults.buttonColors(
                            containerColor = Accent,
                            contentColor   = AccentOnDark,
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text("Ver catálogo de libros", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // ── Categorías Literarias ─────────────────────────────
        if (state.categories.isNotEmpty()) {
            item {
                SectionHeader(title = "Géneros y Categorías", onSeeAll = onCatalogClick)
            }
            item {
                LazyRow(
                    contentPadding      = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.categories.take(6)) { cat ->
                        CategoryChip(
                            name    = cat.nombre,
                            onClick = { onCatalogClick() },
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }

        // ── Últimas Adquisiciones ─────────────────────────────
        item {
            SectionHeader(title = "Últimas Adquisiciones", onSeeAll = onCatalogClick)
        }

        if (state.isLoading) {
            item {
                Box(Modifier.fillMaxWidth().height(200.dp), Alignment.Center) {
                    CircularProgressIndicator(color = Accent)
                }
            }
        } else {
            val chunked = state.books.take(4).chunked(2)
            items(chunked) { row ->
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    row.forEach { book ->
                        BookCard(
                            book     = book,
                            onClick  = { onBookClick(book.id) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(
            text       = title,
            style      = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary,
        )
        TextButton(onClick = onSeeAll) {
            Text("Ver todos", color = Accent, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun CategoryChip(name: String, onClick: () -> Unit) {
    Surface(
        onClick        = onClick,
        shape          = MaterialTheme.shapes.medium,
        color          = Surface2,
        tonalElevation = 0.dp,
        modifier       = Modifier.width(130.dp),
    ) {
        Column(
            modifier            = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("📚", fontSize = 28.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text       = name,
                style      = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary,
                maxLines   = 1,
            )
        }
    }
}

@Composable
fun BookCard(
    book:     Book,
    onClick:  () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick        = onClick,
        shape          = MaterialTheme.shapes.large,
        color          = Surface,
        tonalElevation = 0.dp,
        modifier       = modifier,
    ) {
        Column {
            Box(
                modifier          = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Surface2),
                contentAlignment  = Alignment.Center,
            ) {
                Text("📖", fontSize = 42.sp)

                if (!book.disponible) {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(Error.copy(alpha = 0.85f))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text  = "Prestado / No Disponible",
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text     = book.autor,
                    style    = MaterialTheme.typography.labelSmall,
                    color    = Accent,
                    maxLines = 1,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text       = book.titulo,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextPrimary,
                    maxLines   = 2,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = "Editorial: ${book.editorial}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
                Text(
                    text  = "Año: ${book.anioPublicacion}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextFaint,
                )
            }
        }
    }
}