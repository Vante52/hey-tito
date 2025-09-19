package com.example.heytito.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.heytitoTheme // ✅ Usa tu theme en el Preview

data class Order(
    val id: String,
    val pickupLocation: String,
    val deliveryAddress: String,
    val payment: String,
    val distance: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingDeliveriesScreen(
    onAcceptClick: () -> Unit = {},
    onDetailsClick: () -> Unit = {},
    onRejectClick: () -> Unit = {}
) {
    val orders = remember {
        listOf(
            Order(
                id = "#MNX-24818",
                pickupLocation = "Centro Mayor",
                deliveryAddress = "Calle 127 #15-20, Usaquén",
                payment = "$12.500",
                distance = "12KM"
            ),
            Order(
                id = "#MNX-24819",
                pickupLocation = "Plaza de las Américas",
                deliveryAddress = "Cra 10 #45-12, Chapinero",
                payment = "$8.000",
                distance = "6KM"
            )
        )
    }

    // ✅ Accesos cortos a tokens del theme
    val cs = MaterialTheme.colorScheme
    val typo = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background) // ✅ Fondo desde el theme
    ) {
        // Header resaltado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant), // ✅ Token de color
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pedidos Disponibles",
                    // ✅ Tipografía del theme + peso
                    style = typo.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = cs.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Selecciona las entregas que quieres realizar",
                    style = typo.bodyMedium, // ✅ Jerarquía tipográfica consistente
                    color = cs.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats row
                Card(
                    colors = CardDefaults.cardColors(containerColor = cs.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(value = "12", label = "Pendientes")
                        StatItem(value = "0", label = "En progreso")
                        StatItem(value = "$45.000", label = "Ganado Hoy")
                    }
                }
            }
        }

        // ✅ Divider con outline (evita hardcodear alpha de onSurface)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 2.dp,
            color = cs.outline.copy(alpha = 0.24f)
        )

        // Orders list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(orders) { order ->
                Column {
                    OrderCard(
                        order = order,
//                        onRejectClick = { /* Handle reject */ },
//                        onDetailsClick = { /* Handle details */ },
//                        onAcceptClick = { /* Handle accept */ }
                        onRejectClick = onRejectClick,
                        onDetailsClick = onDetailsClick,
                        onAcceptClick = onAcceptClick
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(top = 12.dp),
                        thickness = 2.dp,
                        color = cs.outline.copy(alpha = 0.24f) // ✅ outline
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onRejectClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onAcceptClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val typo = MaterialTheme.typography

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Order ID
            Text(
                text = order.id,
                style = typo.titleSmall.copy(fontWeight = FontWeight.Medium), // ✅ Tipografía
                color = cs.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pickup location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = cs.onSurfaceVariant, // ✅ Token
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = order.pickupLocation,
                    style = typo.bodyMedium,
                    color = cs.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Delivery address
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = cs.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = order.deliveryAddress,
                    style = typo.bodyMedium,
                    color = cs.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment and distance badges centrados
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                BadgeCard(label = "Pago", value = order.payment)
                BadgeCard(label = "Distancia", value = order.distance)
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons — sin cambiar lógica, solo color desde theme
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors( // ✅ evita Color.Black hardcodeado
                        contentColor = cs.onSurface
                    )
                ) {
                    Text(
                        text = "Rechazar",
                        style = typo.labelLarge,
                        color = cs.onSurface // ✅ token
                    )
                }

                OutlinedButton(
                    onClick = onDetailsClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = cs.onSurface)
                ) {
                    Text(
                        text = "Detalles",
                        style = typo.labelLarge,
                        color = cs.onSurface
                    )
                }

                OutlinedButton(
                    onClick = onAcceptClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = cs.onSurface)
                ) {
                    Text(
                        text = "Aceptar",
                        style = typo.labelLarge,
                        color = cs.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun BadgeCard(label: String, value: String) {
    val cs = MaterialTheme.colorScheme
    val typo = MaterialTheme.typography

    Card(
        colors = CardDefaults.cardColors(containerColor = cs.primary), // ✅ usa primary
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = typo.labelMedium, color = cs.onPrimary) // ✅ onPrimary
            Text(text = value, style = typo.bodyMedium.copy(fontWeight = FontWeight.Bold), color = cs.onPrimary)
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    val cs = MaterialTheme.colorScheme
    val typo = MaterialTheme.typography

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = typo.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = cs.onSurface, // ✅ más contraste para el valor
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            style = typo.labelMedium,
            color = cs.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/* ─────────────────────────── Preview usando tu Theme ─────────────────────────── */

@Preview(showBackground = true, showSystemUi = true, name = "Delivery – Light")
@Composable
private fun CreateProductPreviewLight() {
    heytitoTheme(darkTheme = false, dynamicColor = false) {
        UpcomingDeliveriesScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Delivery – Dark")
@Composable
private fun CreateProductPreviewDark() {
    heytitoTheme(darkTheme = true, dynamicColor = false) {
        UpcomingDeliveriesScreen()
    }
}