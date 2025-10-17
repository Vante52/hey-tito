package com.example.heytito.presentation.ui.screens.vendedor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.heytitoTheme


data class ProductStatus(
    val id: String,
    val name: String,
    val isSelected: Boolean = false
)

data class MyProduct(
    val id: String,
    val title: String,
    val subtitle: String,
    val stock: Int,
    val price: String,
    val soldQuantity: Int,
    val status: String,
    val imageSize: String? = null,
    val hasSpecialBorder: Boolean = false
)

data class ProductAction(
    val name: String,
    val containerColor: Color,
    val contentColor: Color
)

/* ---------------------------------- Screen --------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProductsScreen(
    onBackClick: () -> Unit = {},
    onAddProductClick: () -> Unit = {},
    onProductActionClick: (String, String) -> Unit = { _, _ -> }
) {
    val colors = MaterialTheme.colorScheme
    var selectedStatus by rememberSaveable { mutableStateOf("todos") }

    val statusFilters = listOf(
        ProductStatus("todos", "Todos", selectedStatus == "todos"),
        ProductStatus("publicados", "Publicados", selectedStatus == "publicados"),
        ProductStatus("borradores", "Borradores", selectedStatus == "borradores")
    )

    // Acciones mapeadas a tu tema (sin hardcodes marrones)
    val productActions = remember(colors) {
        listOf(
            ProductAction("Editar",    colors.secondaryContainer, colors.onSecondaryContainer),
            ProductAction("Pausar",    colors.tertiaryContainer,  colors.onTertiaryContainer),
            ProductAction("Duplicar",  colors.surfaceVariant,     colors.onSurfaceVariant),
            ProductAction("Variantes", colors.primaryContainer,   colors.onPrimaryContainer),
            ProductAction("Eliminar",  colors.errorContainer,     colors.onErrorContainer)
        )
    }

    // Mock data (igual que tu ejemplo)
    val products = listOf(
        MyProduct(
            id = "1",
            title = "Pantalón Baggy Negro",
            subtitle = "Pantalón - Denim - 32",
            stock = 12,
            price = "$15.000 COP",
            soldQuantity = 12,
            status = "Publicado",
            hasSpecialBorder = true
        ),
        MyProduct(
            id = "2",
            title = "Pantalón Baggy Negro",
            subtitle = "Pantalón - Denim - 32",
            stock = 12,
            price = "$15.000 COP",
            soldQuantity = 12,
            status = "Publicado",
            imageSize = "371 × 338"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        /* ------------------------------- Top Bar ------------------------------ */
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = colors.surface,
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = colors.onSurface
                    )
                }

                Text(
                    text = "Mis Productos",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onSurface
                    )
                )

                IconButton(onClick = onAddProductClick) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Agregar producto",
                        tint = colors.onSurface
                    )
                }
            }
        }

        /* ------------------------------ Filtros ------------------------------- */
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            items(statusFilters) { status ->
                StatusFilterChip(
                    status = status,
                    onClick = { selectedStatus = status.id }
                )
            }
        }

        /* --------------------------- Lista productos -------------------------- */
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products.size) { idx ->
                val product = products[idx]
                ProductCard(
                    product = product,
                    actions = productActions,
                    onActionClick = { action ->
                        onProductActionClick(product.id, action)
                    }
                )
            }
        }
    }
}

/* --------------------------------- Chips ---------------------------------- */

@Composable
private fun StatusFilterChip(
    status: ProductStatus,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        color = if (status.isSelected) colors.primary.copy(alpha = 0.16f) else colors.surface,
        shape = RoundedCornerShape(18.dp),
        border = if (status.isSelected)
            BorderStroke(1.dp, colors.primary)
        else
            BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                text = status.name,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (status.isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = colors.onSurface
                )
            )
        }
    }
}

/* --------------------------------- Card ----------------------------------- */

@Composable
private fun ProductCard(
    product: MyProduct,
    actions: List<ProductAction>,
    onActionClick: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (product.hasSpecialBorder) {
                    Modifier.border(2.dp, colors.primary, shape)
                } else {
                    Modifier.border(1.dp, colors.outlineVariant, shape)
                }
            ),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                // Placeholder imagen
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(colors.surfaceVariant, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (product.imageSize != null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Imagen",
                                tint = colors.onSurfaceVariant
                            )
                            Text(
                                text = product.imageSize,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = colors.onSurfaceVariant
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Imagen del producto",
                            tint = colors.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                // Info
                Column(Modifier.weight(1f)) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = product.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = colors.onSurfaceVariant
                        )
                    )
                    Spacer(Modifier.height(8.dp))

                    // Badge de estado (usa primaryContainer para que no “tape” el texto)
                    val (badgeBg, badgeFg) = when (product.status.lowercase()) {
                        "publicado" -> colors.primaryContainer to colors.onPrimaryContainer
                        "borrador"  -> colors.surfaceVariant to colors.onSurfaceVariant
                        "pausado"   -> colors.tertiaryContainer to colors.onTertiaryContainer
                        else        -> colors.secondaryContainer to colors.onSecondaryContainer
                    }
                    Surface(
                        color = badgeBg,
                        contentColor = badgeFg,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = product.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Métricas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Metric(label = "Stock", value = product.stock.toString())
                Metric(label = "Precio", value = product.price)
                Metric(label = "Vendidos", value = product.soldQuantity.toString())
            }

            Spacer(Modifier.height(16.dp))

            // Acciones (2 filas)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButton(actions[0], Modifier.weight(1f)) { onActionClick(actions[0].name) }
                    ActionButton(actions[1], Modifier.weight(1f)) { onActionClick(actions[1].name) }
                    ActionButton(actions[2], Modifier.weight(1f)) { onActionClick(actions[2].name) }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButton(actions[3], Modifier.weight(1f)) { onActionClick(actions[3].name) }
                    ActionButton(actions[4], Modifier.weight(1f)) { onActionClick(actions[4].name) }
                    Spacer(Modifier.weight(1f)) // mantiene el grid 3 columnas
                }
            }
        }
    }
}

@Composable
private fun Metric(label: String, value: String) {
    val colors = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                color = colors.onSurfaceVariant
            )
        )
    }
}

/* ------------------------------ Action Button ------------------------------ */

@Composable
private fun ActionButton(
    action: ProductAction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = action.containerColor,
            contentColor = action.contentColor
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = action.name,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

// preview con el theme

@Preview(showBackground = true, showSystemUi = true, name = "MyProducts – Light")
@Composable
private fun MyProductsScreenPreviewLight() {
    heytitoTheme(darkTheme = false, dynamicColor = false) {
        MyProductsScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "MyProducts – Dark")
@Composable
private fun MyProductsScreenPreviewDark() {
    heytitoTheme(darkTheme = true, dynamicColor = false) {
        MyProductsScreen()
    }
}
