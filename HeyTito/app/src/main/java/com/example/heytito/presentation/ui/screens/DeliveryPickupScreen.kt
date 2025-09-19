package com.example.heytito.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryPickupScreen(
    onNavigate: () -> Unit = {},
    onCall: () -> Unit = {},
    onChat: () -> Unit = {},
    onPrimaryCta: () -> Unit = {} // botoncito principal (“Marcar recogido” o “Confirmar entrega”)
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            // AppBar ligero con título centrado
            CenterAlignedTopAppBar(
                title = { Text("Entrega / Recogida", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    // TODO(uni): si esta vista se abre modal, usar Close en vez de Back
                    IconButton(onClick = { /* onBack */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // botoncito de overflow por si metemos “Cancelar viaje”, “Reasignar”, etc.
                    IconButton(onClick = { /* TODO: menú */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            /* ───────────────────── Header de estado ───────────────────── */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(0.dp),
                border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.12f)) // borde suave para separar
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = "Estado del viaje",
                            tint = colors.onSurface
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "En camino a recogida",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "#MIX-24816",
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "12 min",
                            style = MaterialTheme.typography.headlineSmall, // número grandecito
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ETA",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.onSurfaceVariant
                        )
                    }
                }
            }

            /* ───────────────────── Mapa (placeholder) ──────────────────── */
            // color así de suave para que no “tape” el texto; usar onPrimaryContainer para contraste
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Mapa de ubicación",
                        tint = colors.onPrimaryContainer
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Mapa de ubicación",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onPrimaryContainer
                    )
                    // TODO(uni): reemplazar por mapa real (Maps Compose); añadir “Recentrar” como fab
                }
            }

            /* ───────────────────── Sheet de detalles ───────────────────── */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // barrita “handle”
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(colors.onSurfaceVariant.copy(alpha = 0.35f), RoundedCornerShape(2.dp))
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(16.dp))

                    // header pasos
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = colors.onSurface
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Pasos del viaje",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── Cliente (solo borde)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.outline, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // avatar iniciales
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(colors.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "MG",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onPrimary
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "María González · 1d",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "“Entregar en recepción del edificio”",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant
                                )
                                // comentario: botoncito “Ver contacto” podría ir a la derecha si hace falta
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── Pasos (cajas pegadas entre sí, sin separaciones visuales)
                    Column(Modifier.fillMaxWidth()) {
                        StepContainer {
                            TripStep(
                                stepNumber = "1",
                                title = "Recogida en Tortini",
                                address = "CC Centro Mayor, local 201",
                                time = "2:00 – 3:00 p. m.",
                                isActive = true,
                                showConnector = true
                            )
                        }
                        Spacer(Modifier.height(0.dp)) // pegaditas
                        StepContainer {
                            TripStep(
                                stepNumber = "2",
                                title = "Entrega al cliente",
                                address = "Cra 15 #93-47, Apto 302, Chapinero",
                                time = "3:30 – 4:00 p. m.",
                                isActive = false,
                                showConnector = false
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Acciones secundarias
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigate,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.onSurface),
                            border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(24.dp)
                        ) { Text("Navegar") } // botoncito abrir Google Maps/Waze

                        OutlinedButton(
                            onClick = onCall,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.onSurface),
                            border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(24.dp)
                        ) { Text("Llamar") } // botoncito llamar al cliente

                        OutlinedButton(
                            onClick = onChat,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.onSurface),
                            border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(24.dp)
                        ) { Text("Chatear") } // botoncito chat interno
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── CTA principal (llenito) abajo bien visible
                    Button(
                        onClick = onPrimaryCta,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Marcar como recogido", // TODO(uni): cambiar a “Marcar como entregado” según etapa
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/* ────────────────────────── Sub-componentes ─────────────────────────── */

@Composable
private fun StepContainer(content: @Composable () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.outline, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) { content() }
}

/**
 * TripStep: circulito con borde (sin relleno), texto y conector opcional hacia el siguiente paso.
 * showConnector = true dibuja la línea vertical inferior para conectar con el siguiente.
 */
@Composable
fun TripStep(
    stepNumber: String,
    title: String,
    address: String,
    time: String,
    isActive: Boolean,
    showConnector: Boolean = true
) {
    val colors = MaterialTheme.colorScheme
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        // Columna icono + conector
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(40.dp)) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (isActive) colors.primary else colors.outline,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) colors.primary else colors.onSurfaceVariant
                )
            }

            if (showConnector) {
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colors.outline.copy(alpha = 0.8f))
                    )
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        // Contenido textual
        Column(modifier = Modifier.padding(top = 2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurfaceVariant
            )
            // comentario: si el paso está activo, podríamos mostrar un mini botón “Marcar como hecho”
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, name = "DeliveryPickup – Theme Preview")
@Composable
private fun DeliveryPickupPreview() {
    com.example.compose.heytitoTheme {
        DeliveryPickupScreen()
    }
}