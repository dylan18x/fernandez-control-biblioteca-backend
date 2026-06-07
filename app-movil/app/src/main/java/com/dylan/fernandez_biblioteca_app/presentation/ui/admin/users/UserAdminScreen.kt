package com.dylan.fernandez_biblioteca_app.presentation.ui.admin.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dylan.fernandez_biblioteca_app.domain.model.User
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.UserAdminViewModel

@Composable
fun UserAdminScreen(
    viewModel: UserAdminViewModel = hiltViewModel()
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
            if (state.isLoading && state.users.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.users) { user ->
                        UserAdminItem(
                            user = user,
                            onToggleActive = { viewModel.toggleUserActive(user.id) },
                            onDelete = { viewModel.deleteUser(user.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserAdminItem(
    user: User,
    onToggleActive: () -> Unit,
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = user.username, style = MaterialTheme.typography.titleMedium)
                }
                Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = if (user.isStaff) "Administrador" else "Lector",
                    color = if (user.isStaff) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = if (user.isActive) "Activo" else "Inactivo",
                    color = if (user.isActive) Color(0xFF4CAF50) else Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row {
                IconButton(onClick = onToggleActive) {
                    Icon(
                        if (user.isActive) Icons.Default.Block else Icons.Default.CheckCircle,
                        contentDescription = "Toggle Active",
                        tint = if (user.isActive) Color.Red else Color(0xFF4CAF50)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
