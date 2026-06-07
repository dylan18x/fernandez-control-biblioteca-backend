package com.dylan.fernandez_biblioteca_app.presentation.ui.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dylan.fernandez_biblioteca_app.presentation.components.LoadingScreen
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.DashboardUiState
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.DashboardViewModel
import com.dylan.fernandez_biblioteca_app.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    viewModel:  DashboardViewModel = hiltViewModel(),
) {
    val state       by viewModel.state.collectAsState()
    val lastUpdated by viewModel.lastUpdated.collectAsState()

    when (val s = state) {
        is DashboardUiState.Loading ->
            LoadingScreen("Cargando dashboard...")
        is DashboardUiState.Error   -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⚠️ ${s.message}", color = Error)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = viewModel::load,
                        colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                        Text("Reintentar", color = AccentOnDark)
                    }
                }
            }
        }
        is DashboardUiState.Success ->
            DashboardContent(
                stats       = s.stats,
                lastUpdated = lastUpdated,
                onNavigate  = onNavigate,
                onRefresh   = viewModel::load,
            )
    }
}

@Composable
private fun DashboardContent(
    stats:       com.dylan.fernandez_biblioteca_app.presentation.viewmodel.DashboardStats,
    lastUpdated: Long,
    onNavigate:  (String) -> Unit,
    onRefresh:   () -> Unit,
) {
    val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val timeStr = if (lastUpdated > 0) timeFmt.format(Date(lastUpdated)) else "—"

    LazyColumn(
        modifier       = Modifier.fillMaxSize().background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header
        item {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text       = "Dashboard",
                        style      = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                    )
                    Text(
                        text  = "Actualizado: $timeStr",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextFaint,
                    )
                }
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Actualizar", tint = Accent)
                }
            }
        }

        // ── KPIs — fila 1 ─────────────────────────────────────
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(
                    title    = "Libros totales",
                    value    = stats.totalBooks.toString(),
                    subtitle = "${stats.totalActiveBooks} disponibles",
                    icon     = Icons.Default.LibraryBooks,
                    color    = Accent,
                    onClick  = { onNavigate("admin/books") },
                    modifier = Modifier.weight(1f),
                )
                KpiCard(
                    title   = "Categorías",
                    value   = stats.totalCategories.toString(),
                    icon    = Icons.Default.Category,
                    color   = Info,
                    onClick = { onNavigate("admin/categories") },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // ── KPIs — fila 2 ─────────────────────────────────────
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(
                    title    = "Préstamos",
                    value    = stats.totalLoans.toString(),
                    subtitle = if (stats.pendingLoans > 0)
                               "${stats.pendingLoans} pendientes" else null,
                    icon     = Icons.Default.Assignment,
                    color    = Success,
                    hasAlert = stats.pendingLoans > 0,
                    onClick  = { onNavigate("admin/loans") },
                    modifier = Modifier.weight(1f),
                )
                KpiCard(
                    title    = "Usuarios",
                    value    = stats.totalUsers.toString(),
                    subtitle = "${stats.activeUsers} activos",
                    icon     = Icons.Default.People,
                    color    = Warning,
                    onClick  = { onNavigate("admin/users") },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // ── Préstamos por estado ─────────────
        if (stats.loansByStatus.isNotEmpty()) {
            item {
                Surface(
                    color    = Surface,
                    shape    = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically,
                        ) {
                            Text(
                                text       = "Préstamos por estado",
                                style      = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary,
                            )
                            TextButton(onClick = { onNavigate("admin/loans") }) {
                                Text("Ver todos", color = Accent,
                                    style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(Modifier.height(16.dp))

                        val total = stats.totalLoans.coerceAtLeast(1)
                        stats.loansByStatus.entries.forEach { (status, count) ->
                            val color = when(status) {
                                "DEVUELTO" -> Success
                                "ACTIVO" -> Info
                                "PENDIENTE" -> Warning
                                "ATRASADO" -> Error
                                else -> TextSecondary
                            }
                            val pct = (count.toFloat() / total).coerceIn(0.02f, 1f)

                            Column(modifier = Modifier.padding(bottom = 10.dp)) {
                                Row(
                                    modifier              = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text  = status,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                    )
                                    Text(
                                        text       = count.toString(),
                                        style      = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color      = color,
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(7.dp)
                                        .background(Surface2, MaterialTheme.shapes.extraSmall),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(pct)
                                            .fillMaxHeight()
                                            .background(color, MaterialTheme.shapes.extraSmall),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Libros Recientes / Alertas ───────────────────────────
        item {
            Surface(
                color    = Surface,
                shape    = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Book,
                                contentDescription = null,
                                tint    = Accent,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text       = "Libros disponibles",
                                style    = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary,
                            )
                        }
                        TextButton(onClick = { onNavigate("admin/books") }) {
                            Text("Gestionar", color = Accent,
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    if (stats.lowStockBooks.isEmpty()) {
                        Box(
                            modifier         = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("📚 No hay libros registrados", color = TextSecondary,
                                style = MaterialTheme.typography.bodySmall)
                        }
                    } else {
                        Spacer(Modifier.height(8.dp))
                        stats.lowStockBooks.forEach { book ->
                            Surface(
                                onClick  = { onNavigate("admin/books") },
                                color    = Surface2,
                                shape    = MaterialTheme.shapes.medium,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                            ) {
                                Row(
                                    modifier              = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment     = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text     = book.title,
                                        style    = MaterialTheme.typography.bodySmall,
                                        color    = TextPrimary,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                    )
                                    Surface(
                                        color = if (book.isAvailable) Success.copy(alpha = 0.15f)
                                                else Error.copy(alpha = 0.15f),
                                        shape = MaterialTheme.shapes.extraSmall,
                                    ) {
                                        Text(
                                            text       = if (book.isAvailable) "Disponible" else "Prestado",
                                            color      = if (book.isAvailable) Success else Error,
                                            fontSize   = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Acciones rápidas ──────────────────────────────────
        item {
            Surface(
                color    = Surface,
                shape    = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = "⚡ Acciones rápidas",
                        style      = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        modifier   = Modifier.padding(bottom = 12.dp),
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(listOf(
                            Triple("+ Categoría", Info,    "admin/categories"),
                            Triple("+ Libro",     Accent,  "admin/books"),
                            Triple("Préstamos",   Success, "admin/loans"),
                            Triple("Usuarios",    Warning, "admin/users"),
                        )) { (label, color, route) ->
                            Surface(
                                onClick  = { onNavigate(route) },
                                color    = color.copy(alpha = 0.1f),
                                shape    = MaterialTheme.shapes.medium,
                            ) {
                                Text(
                                    text       = label,
                                    color      = color,
                                    fontWeight = FontWeight.Bold,
                                    style      = MaterialTheme.typography.bodySmall,
                                    modifier   = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}