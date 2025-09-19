package com.example.heytito.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.material.icons.automirrored.filled.Help as AutoMirroredHelp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.heytitoTheme

data class ProfileSection(
    val title: String,
    val items: List<ProfileItem>
)

data class ProfileItem(
    val label: String,
    val value: String,
    val icon: ImageVector? = null,
    val isEditable: Boolean = true,
    val onClick: () -> Unit = {}
)

// ⚠️ Sin @Preview aquí. Los previews van al final envueltos en heytitoTheme.
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    var selectedRole by remember { mutableStateOf("Vendedor") }
    var selectedSizes by remember { mutableStateOf(setOf("S", "M", "L")) }

    val personalInfo = ProfileSection(
        title = "INFORMACIÓN PERSONAL",
        items = listOf(
            ProfileItem("NOMBRE", "Mariana Rodriguez"),
            ProfileItem("EMAIL", "mariana.rodriguez@email.com"),
            ProfileItem("TELÉFONO", "+57 300 123 4567"),
            ProfileItem("UBICACIÓN", "Medellín, Colombia"),
            ProfileItem("ROL", selectedRole)
        )
    )

    val paymentMethods = listOf(
        "•••• •••• •••• 1234" to "Eliminar",
        "•••• •••• •••• 5678" to "Eliminar"
    )

    val shippingInfo = ProfileSection(
        title = "DIRECCIONES DE ENVÍO",
        items = listOf(
            ProfileItem("DIRECCIÓN PRINCIPAL", "Calle 45 #23-67, Laureles"),
            ProfileItem("DIRECCIÓN SECUNDARIA", "Agregar dirección adicional")
        )
    )

    val sizes = listOf("XS", "S", "M", "L", "XL", "XXL", "28", "30")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background), // antes: Color(0xFFF5F5DC)
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header con botón de regreso
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = colors.onSurface // Nota estudiante: usar onSurface para respetar tema
                    )
                }
                Text(
                    text = "Configuración",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp)) // para balancear el IconButton izquierdo
            }
        }

        item {
            // Perfil del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar (placeholder)
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(colors.primary.copy(alpha = 0.12f)), // color suave para que no “tape”
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👩‍🦰",
                            fontSize = 40.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Mariana, 26",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )

                    Text(
                        text = "Comprador",
                        fontSize = 14.sp,
                        color = colors.onSurfaceVariant
                    )
                }
            }
        }

        item {
            // Información Personal
            ProfileSectionCard(
                section = personalInfo,
                onItemClick = { /* TODO: abrir pantallita de edición */ }
            )
        }

        item {
            // Métodos de Pago
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "MÉTODOS DE PAGO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    paymentMethods.forEach { (cardNumber, action) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CreditCard,
                                    contentDescription = null,
                                    tint = colors.primary, // Nota estudiante: iconito con primary, no tan fuerte
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = cardNumber,
                                    fontSize = 14.sp,
                                    color = colors.onSurface
                                )
                            }

                            TextButton(onClick = { /* TODO: eliminar tarjeta */ }) {
                                Text(
                                    text = action,
                                    color = colors.primary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: agregar método de pago */ }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Agregar método de pago",
                            fontSize = 14.sp,
                            color = colors.primary
                        )
                    }
                    // Nota estudiante: botoncito de “Ver todos” si la lista crece
                }
            }
        }

        item {
            // Direcciones de Envío
            ProfileSectionCard(
                section = shippingInfo,
                onItemClick = { /* TODO: abrir detalle/edición de dirección */ }
            )
        }

        item {
            // Tallas Preferidas
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TALLAS PREFERIDAS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Grid de tallas (2 filas)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sizes.take(4).forEach { size ->
                            SizeChip(
                                size = size,
                                isSelected = selectedSizes.contains(size),
                                onToggle = {
                                    selectedSizes =
                                        if (selectedSizes.contains(size)) selectedSizes - size
                                        else selectedSizes + size
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sizes.drop(4).forEach { size ->
                            SizeChip(
                                size = size,
                                isSelected = selectedSizes.contains(size),
                                onToggle = {
                                    selectedSizes =
                                        if (selectedSizes.contains(size)) selectedSizes - size
                                        else selectedSizes + size
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        item {
            // Configuración
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CONFIGURACIÓN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "IDIOMA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: abrir selector de idioma */ }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Español",
                            fontSize = 14.sp,
                            color = colors.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = colors.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            // Botón Guardar Cambios
            Button(
                onClick = { /* TODO: guardar cambios */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Guardar Cambios",
                    color = colors.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item {
            // Ayuda y Soporte
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "AYUDA Y SOPORTE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: abrir centro de ayuda */ }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = null,
                            tint = colors.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Centro de ayuda",
                            fontSize = 14.sp,
                            color = colors.onSurface
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: abrir FAQ */ }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.QuestionAnswer,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "FAQ y tutoriales",
                            fontSize = 14.sp,
                            color = colors.onSurface
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: contactar soporte */ }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Support,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Contactar soporte",
                            fontSize = 14.sp,
                            color = colors.onSurface
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: enviar mensaje */ }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Enviar mensaje",
                            fontSize = 14.sp,
                            color = colors.onSurface
                        )
                    }
                }
            }
        }

        item {
            // Botones de acción
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Cerrar Sesión
                OutlinedButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.primary // texto/ícono
                    ),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        width = 1.dp,
                        brush = SolidColor(colors.primary)
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Eliminar cuenta
                TextButton(
                    onClick = onDeleteAccountClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.error
                    )
                ) {
                    Text(
                        text = "Eliminar cuenta",
                        fontSize = 14.sp
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun ProfileSectionCard(
    section: ProfileSection,
    onItemClick: (ProfileItem) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = section.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            section.items.forEachIndexed { index, item ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = item.isEditable) { onItemClick(item) }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.value,
                                fontSize = 14.sp,
                                color = colors.onSurface
                            )
                        }

                        if (item.isEditable) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = colors.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    if (index < section.items.size - 1) {
                        HorizontalDivider(
                            color = colors.outlineVariant,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SizeChip(
    size: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    // Nota estudiante: seleccionado -> primary/onPrimary; no seleccionado -> surface + borde outline
    Card(
        modifier = modifier
            .clickable { onToggle() }
            .height(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colors.primary else colors.surface
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        ),
        border = if (!isSelected)
            androidx.compose.foundation.BorderStroke(1.dp, colors.outline)
        else null
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = size,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) colors.onPrimary else colors.onSurface
            )
        }
    }
}


// Previews con heytitoTheme
@Preview(showBackground = true, name = "Profile – Light (Brand)")
@Composable
private fun ProfilePreviewLight() {
    heytitoTheme(darkTheme = false, dynamicColor = false) {
        ProfileScreen()
    }
}

@Preview(showBackground = true, name = "Profile – Dark (Brand)")
@Composable
private fun ProfilePreviewDark() {
    heytitoTheme(darkTheme = true, dynamicColor = false) {
        ProfileScreen()
    }
}
