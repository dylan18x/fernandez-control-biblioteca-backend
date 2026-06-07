package com.dylan.fernandez_biblioteca_app.presentation.ui.admin.categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBook
import com.dylan.fernandez_biblioteca_app.domain.model.CategoryBookPayload
import com.dylan.fernandez_biblioteca_app.presentation.components.BibliotecaButton
import com.dylan.fernandez_biblioteca_app.presentation.components.BibliotecaTextField
import com.dylan.fernandez_biblioteca_app.presentation.viewmodel.CategoryFormState
import com.dylan.fernandez_biblioteca_app.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFormSheet(
    initial:    CategoryBook?,
    formState:  CategoryFormState,
    onSave:     (CategoryBookPayload) -> Unit,
    onDismiss:  () -> Unit,
) {
    val isEdit = initial != null

    var name        by remember { mutableStateOf(initial?.name        ?: "") }
    var description by remember { mutableStateOf(initial?.description ?: "") }

    // Cerrar al éxito
    LaunchedEffect(formState) {
        if (formState is CategoryFormState.Success) onDismiss()
    }

    val isSaving = formState is CategoryFormState.Saving
    val nameError = name.isNotEmpty() && name.length < 2
    val canSave   = name.length >= 2 && !nameError && !isSaving

    ModalBottomSheet(
        onDismissRequest = { if (!isSaving) onDismiss() },
        containerColor   = Surface,
        dragHandle       = {
            Box(
                modifier         = Modifier
                    .padding(vertical = 12.dp)
                    .size(40.dp, 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    modifier = Modifier.size(40.dp, 4.dp),
                    color    = Border,
                    shape    = MaterialTheme.shapes.extraSmall,
                ) {}
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Título
            Text(
                text       = if (isEdit) "Editar: ${initial?.name}" else "Nueva categoría",
                style      = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
            )

            // Error del formulario
            if (formState is CategoryFormState.Error) {
                Surface(
                    color  = Error.copy(alpha = 0.1f),
                    shape  = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text     = formState.message,
                        color    = Error,
                        style    = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            // Nombre
            BibliotecaTextField(
                value         = name,
                onValueChange = { name = it },
                label         = "Nombre *",
                placeholder   = "ej. Novela Histórica",
                isError       = nameError,
                errorMessage  = "Mínimo 2 caracteres",
                enabled       = !isSaving,
            )

            // Descripción
            OutlinedTextField(
                value         = description,
                onValueChange = { description = it },
                label         = { Text("Descripción") },
                placeholder   = { Text("Descripción opcional", color = TextFaint) },
                minLines      = 3,
                maxLines      = 5,
                enabled       = !isSaving,
                modifier      = Modifier.fillMaxWidth(),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = Accent,
                    focusedLabelColor    = Accent,
                    unfocusedBorderColor = Border,
                    unfocusedLabelColor  = TextSecondary,
                ),
            )

            // Botones
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick  = { if (!isSaving) onDismiss() },
                    enabled  = !isSaving,
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    border   = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Border),
                    ),
                    shape    = MaterialTheme.shapes.medium,
                ) {
                    Text("Cancelar")
                }
                BibliotecaButton(
                    text     = if (isSaving) "Guardando..." else if (isEdit) "Guardar" else "Crear",
                    onClick  = {
                        onSave(CategoryBookPayload(name.trim(), description.trim()))
                    },
                    enabled  = canSave,
                    isLoading = isSaving,
                    modifier = Modifier.weight(1f).height(52.dp)
                )
            }
        }
    }
}
