package com.dylan.fernandez_biblioteca_app.presentation.ui.admin.books

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dylan.fernandez_biblioteca_app.domain.model.Book
import com.dylan.fernandez_biblioteca_app.domain.model.BookPayload
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBook
import com.dylan.fernandez_biblioteca_app.presentation.components.BibliotecaTextField
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.BookAdminFormState
import com.dylan.fernandez_biblioteca_app.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFormSheet(
    initial:    Book?,
    categories: List<CategoryBook>,
    formState:  BookAdminFormState,
    onSave:     (BookPayload) -> Unit,
    onDismiss:  () -> Unit,
) {
    val isEdit = initial != null

    var title           by remember { mutableStateOf(initial?.title ?: "") }
    var author          by remember { mutableStateOf(initial?.author ?: "") }
    var publisher       by remember { mutableStateOf(initial?.publisher ?: "") }
    var publicationYear by remember { mutableStateOf(initial?.publicationYear?.toString() ?: "") }
    var isAvailable     by remember { mutableStateOf(initial?.isAvailable ?: true) }
    var selectedCat     by remember { mutableStateOf(initial?.categoryId) }
    var catExpanded     by remember { mutableStateOf(false) }

    val isSaving   = formState is BookAdminFormState.Saving
    val yearVal    = publicationYear.toIntOrNull()
    
    val titleError  = title.isNotEmpty() && title.length < 2
    val authorError = author.isNotEmpty() && author.length < 2
    val yearError   = publicationYear.isNotEmpty() && (yearVal == null || yearVal < 1000 || yearVal > 2100)
    
    val canSave    = title.length >= 2 && author.length >= 2 && 
                     yearVal != null && yearVal in 1000..2100 &&
                     selectedCat != null && !isSaving

    LaunchedEffect(formState) {
        if (formState is BookAdminFormState.Success) onDismiss()
    }

    ModalBottomSheet(
        onDismissRequest = { if (!isSaving) onDismiss() },
        containerColor   = Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text       = if (isEdit) "Editar: ${initial?.title}" else "Nuevo libro",
                style      = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
            )

            if (formState is BookAdminFormState.Error) {
                Surface(
                    color    = Error.copy(alpha = 0.1f),
                    shape    = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        formState.message, color = Error,
                        style    = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            BibliotecaTextField(
                value         = title,
                onValueChange = { title = it },
                label         = "Título *",
                placeholder   = "ej. Cien años de soledad",
                isError       = titleError,
                errorMessage  = "Mínimo 2 caracteres",
                enabled       = !isSaving,
            )

            BibliotecaTextField(
                value         = author,
                onValueChange = { author = it },
                label         = "Autor *",
                placeholder   = "ej. Gabriel García Márquez",
                isError       = authorError,
                errorMessage  = "Mínimo 2 caracteres",
                enabled       = !isSaving,
            )

            BibliotecaTextField(
                value         = publisher,
                onValueChange = { publisher = it },
                label         = "Editorial",
                placeholder   = "ej. Sudamericana",
                enabled       = !isSaving,
            )

            BibliotecaTextField(
                value         = publicationYear,
                onValueChange = { publicationYear = it },
                label         = "Año de publicación *",
                placeholder   = "ej. 1967",
                isError       = yearError,
                errorMessage  = "Año inválido",
                enabled       = !isSaving,
                keyboardType  = KeyboardType.Number,
            )

            ExposedDropdownMenuBox(
                expanded         = catExpanded,
                onExpandedChange = { catExpanded = !catExpanded },
            ) {
                OutlinedTextField(
                    value         = categories.find { it.id == selectedCat }?.name ?: "— Seleccionar Categoría —",
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Categoría *") },
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Accent,
                        unfocusedBorderColor = if (selectedCat == null) Error else Border,
                        focusedLabelColor    = Accent,
                        unfocusedLabelColor  = TextSecondary,
                        focusedTextColor     = TextPrimary,
                        unfocusedTextColor   = TextPrimary,
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                )
                ExposedDropdownMenu(
                    expanded         = catExpanded,
                    onDismissRequest = { catExpanded = false },
                    modifier         = Modifier.background(Surface)
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    cat.name,
                                    color      = if (selectedCat == cat.id) Accent else TextPrimary,
                                    fontWeight = if (selectedCat == cat.id) FontWeight.Bold else FontWeight.Normal,
                                )
                            },
                            onClick = { selectedCat = cat.id; catExpanded = false },
                        )
                    }
                }
            }

            Surface(
                color    = Surface2,
                shape    = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            "Libro disponible",
                            style      = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color      = TextPrimary,
                        )
                        Text(
                            "Visible para préstamos",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                        )
                    }
                    Switch(
                        checked         = isAvailable,
                        onCheckedChange = { isAvailable = it },
                        enabled         = !isSaving,
                        colors          = SwitchDefaults.colors(
                            checkedThumbColor    = AccentOnDark,
                            checkedTrackColor    = Accent,
                            uncheckedTrackColor  = Surface,
                            uncheckedBorderColor = Border,
                        ),
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick  = { if (!isSaving) onDismiss() },
                    enabled  = !isSaving,
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    border   = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Border),
                    ),
                    shape = MaterialTheme.shapes.medium,
                ) { Text("Cancelar") }

                Button(
                    onClick = {
                        onSave(BookPayload(
                            title           = title.trim(),
                            author          = author.trim(),
                            publisher       = publisher.trim(),
                            publicationYear = yearVal!!,
                            isAvailable     = isAvailable,
                            categoryId      = selectedCat!!,
                        ))
                    },
                    enabled  = canSave,
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = Accent,
                        contentColor           = AccentOnDark,
                        disabledContainerColor = Accent.copy(alpha = 0.4f),
                    ),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color       = AccentOnDark,
                            modifier    = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        if (isSaving) "Guardando..."
                        else if (isEdit) "Guardar cambios"
                        else "Crear libro",
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
