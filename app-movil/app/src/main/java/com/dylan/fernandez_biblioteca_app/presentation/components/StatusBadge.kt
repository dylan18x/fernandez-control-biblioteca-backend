package com.dylan.fernandez_biblioteca_app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dylan.fernandez_biblioteca_app.theme.*

/**
 * Representa los estados posibles de un préstamo en la UI
 */
enum class LoanStatus(val label: String, val color: Color) {
    PENDIENTE("Pendiente", Warning),
    DEVUELTO("Devuelto", Success),
    ACTIVO("Activo", Info),
    ATRASADO("Atrasado", Error);

    companion object {
        fun fromString(status: String): LoanStatus {
            return try {
                valueOf(status.uppercase())
            } catch (e: Exception) {
                ACTIVO // Estado por defecto
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String, 
    modifier: Modifier = Modifier
) {
    val loanStatus = LoanStatus.fromString(status)
    val color = loanStatus.color

    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, RoundedCornerShape(50)),
        )
        Text(
            text = loanStatus.label,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.3.sp,
        )
    }
}
