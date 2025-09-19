package com.example.heytito.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.heytitoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilityScreen(
    onSave: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    // usar saveable para no perder estado al rotar
    var available by rememberSaveable { mutableStateOf(true) }
    var quickSlot by rememberSaveable { mutableStateOf<QuickSlot?>(null) }
    var startTime by rememberSaveable { mutableStateOf("08:00 a. m.") }
    var endTime by rememberSaveable { mutableStateOf("08:00 p. m.") }

    Scaffold(
        containerColor = colors.background,
        topBar = {
            // top bar sencillita; título centrado
            CenterAlignedTopAppBar(
                title = { Text("Disponibilidad", fontWeight = FontWeight.SemiBold) }
                // TODO(uni): si hace falta back, poner navigationIcon con ArrowBack
            )
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Estado general ────────────────────────────────────────────────────────
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // circulito de status
                Box(
                    Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        // color suave para que no “tape” todo; si está activo, primario con alpha
                        .background(
                            if (available) colors.primary.copy(alpha = 0.22f)
                            else colors.outline.copy(alpha = 0.25f)
                        )
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = if (available) "¡Estás disponible!" else "No estás disponible",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                // podríamos mostrar subtexto tipo “se mostrará en tu perfil” para dar contexto
            }

            HorizontalDivider(
                Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                DividerDefaults.Thickness, color = colors.outline.copy(alpha = 0.3f)
            )

            // ── Card: Switch de disponibilidad ───────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.25f))
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Estado de disponibilidad",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.weight(1f))
                    // botoncito de on/off
                    Switch(
                        checked = available,
                        onCheckedChange = { available = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = colors.primary,
                            checkedThumbColor = colors.onPrimary,
                            uncheckedThumbColor = colors.surface
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Card: métrica del día (suave) ────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerHigh),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.25f))
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Ganancias de hoy",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "$45.000 COP",
                        style = MaterialTheme.typography.bodyLarge.copy(color = colors.onSurfaceVariant)
                    )
                    // TODO(uni): traer esto del backend y formatear con locale
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Preferencias de horario ──────────────────────────────────────────────
            Text(
                "Horarios preferidos",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Horarios rápidos",
                style = MaterialTheme.typography.bodySmall.copy(color = colors.onSurfaceVariant)
            )
            Spacer(Modifier.height(10.dp))

            // Pills con wrap (cuando tengamos Compose FlowRow estable, cambiarlo)
            FlowRowWrap(horizontalGap = 10.dp, verticalGap = 10.dp) {
                QuickSlot.entries.forEach { slot ->
                    SelectablePill(
                        text = slot.label,
                        selected = quickSlot == slot,
                        onClick = {
                            // botoncito: si ya está seleccionado, quitarlo; si no, ponerlo
                            quickSlot = if (quickSlot == slot) null else slot
                            // comentario: al tocar un quick slot, podríamos setear start/end automáticamente
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Card: horario personalizado ─────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.25f))
            ) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        "Horario personalizado",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimePill(
                            text = startTime,
                            onClick = {
                                // TODO(uni): abrir TimePicker (Material3) para seleccionar hora de inicio
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("—", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.width(8.dp))
                        TimePill(
                            text = endTime,
                            onClick = {
                                // TODO(uni): abrir TimePicker (Material3) para seleccionar hora fin
                            }
                        )
                    }
                    // comentario: validar que end > start; si no, mostrar error en rojo suave
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── CTA Guardar ──────────────────────────────────────────────────────────
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                enabled = true // comentario: deshabilitar si no hay cambios
            ) {
                Text(
                    "Guardar horarios",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

/* ────────────────────────────── Soportes UI ─────────────────────────────── */

private enum class QuickSlot(val label: String) {
    MANANA("Mañana (6–12)"),
    TARDE("Tarde (12–18)"),
    NOCHE("Noche (18–00)"),
    TODO_DIA("Todo el día");
}

@Composable
private fun SelectablePill(text: String, selected: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) colors.primary.copy(alpha = 0.16f) else colors.surface,
        border = if (selected)
            BorderStroke(1.dp, colors.primary)
        else
            BorderStroke(1.dp, colors.outline.copy(alpha = 0.7f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        )
    }
    // comentario: si metemos muchos, poner opción “Limpiar” al final
}

@Composable
private fun TimePill(text: String, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = colors.primary.copy(alpha = 0.10f), // color así de suave para que no tape
        border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.6f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
        )
    }
    // botoncito: al tocar acá debería abrir el selector de hora (TimePicker)
}

/**
 * Layout de flujo simple. Nota: cuando uses compose foundation reciente,
 * se puede cambiar a androidx.compose.foundation.layout.FlowRow (menos código).
 */
@Composable
private fun FlowRowWrap(
    horizontalGap: Dp = 8.dp,
    verticalGap: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(content = content) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val maxWidth = constraints.maxWidth
        var rowHeight = 0
        var x = 0
        var y = 0
        val positions = mutableListOf<Pair<Int, Int>>()

        placeables.forEach { p ->
            if (x + p.width > maxWidth) {
                x = 0
                y += rowHeight + verticalGap.roundToPx()
                rowHeight = 0
            }
            positions.add(x to y)
            x += p.width + horizontalGap.roundToPx()
            rowHeight = maxOf(rowHeight, p.height)
        }

        val totalHeight = y + rowHeight
        layout(width = maxWidth, height = totalHeight) {
            placeables.forEachIndexed { index, p ->
                val (px, py) = positions[index]
                p.place(px, py)
            }
        }
    }
}

/* ─────────────────────────────── Preview ────────────────────────────────── */

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_6")
@Composable
private fun AvailabilityPreview() {
    heytitoTheme {
        AvailabilityScreen()
    }
}
