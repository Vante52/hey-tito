package com.example.heytito.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.heytitoTheme

// ────────────────────────────────────────────────────────────────────────────────
// DATA
// ────────────────────────────────────────────────────────────────────────────────
data class StoreProfileList(
    val name: String,
    val followers: String,
    val following: String,
    val rating: String,
    val description: String,
    val hasNewPublications: Boolean,
    val mascotMessage: String
)

data class StoreProductList(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String? = null,
    val likes: Int = 0,
    val comments: Int = 0,
    val hasDiscount: Boolean = false
)

// ────────────────────────────────────────────────────────────────────────────────
// SCREEN
// ────────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClienteDashboardScreen(
    onBackClick: () -> Unit = {},
    onFollowClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
    onOpenComments: (String) -> Unit = {},
    onAddToCart: (String) -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    var query by remember { mutableStateOf("") }

    // Chips demo
    var selected by remember { mutableStateOf(setOf("Streetwear")) }
    val chips = listOf("Streetwear", "Vintage", "Deportivo")

    // Demo data
    val products = remember {
        listOf(
            StoreProductList("1", "Atelier Nova", "$189.900", hasDiscount = true, likes = 120, comments = 14),
            StoreProductList("2", "Luna Urban", "$239.900", likes = 44, comments = 3),
            StoreProductList("3", "EcoWear", "$129.900", likes = 8, comments = 0),
            StoreProductList("4", "VintageSoul", "$99.900", hasDiscount = true, likes = 76, comments = 9),
            StoreProductList("5", "UrbanX", "$149.900", likes = 3, comments = 0),
            StoreProductList("6", "Minimal Co.", "$209.900", likes = 28, comments = 2)
        )
    }

    Scaffold(
        topBar = {
            Surface(color = colors.surface, shadowElevation = 1.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                    SearchRow(
                        query = query,
                        onQueryChange = { query = it },
                        onFilterClick = onFilterClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    ) { inner ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 0.dp, end = 0.dp,
                top = inner.calculateTopPadding(),
                bottom = inner.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Chips (full width)
            item {
                FilterChipsRow(
                    chips = chips,
                    selected = selected,
                    onToggle = { label ->
                        selected = selected.toMutableSet().apply {
                            if (contains(label)) remove(label) else add(label)
                        }
                    }
                )
                Spacer(Modifier.height(4.dp))
            }

            // Feed estilo Instagram
            items(products, key = { it.id }) { product ->
                InstaPostCard(
                    product = product,
                    onAvatarClick = onStoreClick,
                    onThreeDotsClick = onStoreClick,
                    onOpenComments = { onOpenComments(product.id) },
                    onAddToCart = { onAddToCart(product.id) },
                    onImageClick = { onProductClick(product.id) }
                )
            }
        }
    }
}

// ────────────────────────────────────────────────────────────────────────────────
// COMPONENTES
// ────────────────────────────────────────────────────────────────────────────────
@Composable
private fun FilterChipsRow(
    chips: List<String>,
    selected: Set<String>,
    onToggle: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        chips.forEach { label ->
            FilterChip(
                selected = selected.contains(label),
                onClick = { onToggle(label) },
                label = { Text(label) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InstaPostCard(
    product: StoreProductList,
    onAvatarClick: () -> Unit,
    onThreeDotsClick: () -> Unit,
    onOpenComments: () -> Unit,
    onAddToCart: () -> Unit,
    onImageClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    // Estado local “like / bookmark” por post
    var liked by remember(product.id) { mutableStateOf(false) }
    var bookmarked by remember(product.id) { mutableStateOf(false) }
    var likeCount by remember(product.id) { mutableStateOf(product.likes) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(0.dp), // borde recto como IG
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // Header (avatar + nombre + menú)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceVariant)
                    .clickable { onAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = colors.onSurfaceVariant)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("• hace 2 h", fontSize = 11.sp, color = colors.onSurfaceVariant)
            }
            IconButton(onClick = onThreeDotsClick) {
                Icon(Icons.Outlined.MoreVert, contentDescription = "Más opciones")
            }
        }

        // Carrusel cuadrado como Instagram
        val pagerState = rememberPagerState(pageCount = { 3 })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable { onImageClick() }
        ) {
            HorizontalPager(state = pagerState) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            when (page % 3) {
                                0 -> colors.surfaceVariant.copy(alpha = 0.45f)
                                1 -> colors.tertiaryContainer.copy(alpha = 0.45f)
                                else -> colors.primaryContainer.copy(alpha = 0.45f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Foto ${page + 1}")
                }
            }

            // Badge Oferta (arriba derecha)
            if (product.hasDiscount) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(colors.error, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("OFERTA", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = colors.onError)
                }
            }

            // Indicador de páginas (abajo-centro)
            PagerIndicator(
                pageCount = pagerState.pageCount,
                currentPageIndex = pagerState.currentPage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            )
        }

        // Fila de acciones (like, comentar, enviar, bookmark)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                liked = !liked
                likeCount = if (liked) likeCount + 1 else (likeCount - 1).coerceAtLeast(0)
            }) {
                Icon(
                    imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (liked) "Quitar me gusta" else "Dar me gusta",
                    tint = if (liked) MaterialTheme.colorScheme.error else LocalContentColor.current
                )
            }
            IconButton(onClick = onOpenComments) {
                Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Comentarios")
            }
            IconButton(onClick = { /* compartir */ }) {
                Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Enviar")
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { bookmarked = !bookmarked }) {
                Icon(
                    imageVector = if (bookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = if (bookmarked) "Guardado" else "Guardar"
                )
            }
        }

        // Likes
        Text(
            text = "$likeCount Me gusta",
            modifier = Modifier.padding(horizontal = 12.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        // Caption + precio
        Column(Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            Text(text = "${product.name}  •  Nueva colección", fontSize = 13.sp)
            Spacer(Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("Precio", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(product.price, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                FilledTonalButton(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Añadir al carrito")
                }
            }
        }

        // CTA “Ver comentarios”
        if (product.comments > 0) {
            TextButton(onClick = onOpenComments, modifier = Modifier.padding(horizontal = 4.dp)) {
                Text("Ver los ${product.comments} comentarios")
            }
        }

        // Separador sutil
        Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}

// Indicador de páginas sencillo (tu ejemplo adaptado)
@Composable
fun PagerIndicator(pageCount: Int, currentPageIndex: Int, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (currentPageIndex == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchRow(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        trailingIcon = {
            IconButton(onClick = onFilterClick) { Icon(Icons.Default.MoreVert, contentDescription = "Filtros") }
        },
        placeholder = { Text("Buscar marcas, estilos…") },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.outline,
            unfocusedBorderColor = colors.outlineVariant
        )
    )
}

// ────────────────────────────────────────────────────────────────────────────────
// PREVIEWS
// ────────────────────────────────────────────────────────────────────────────────
@Preview(showBackground = true, name = "Cliente – Light")
@Composable
private fun ClientePreviewLight() {
    heytitoTheme(darkTheme = false, dynamicColor = false) {
        ClienteDashboardScreen(onFilterClick = {})
    }
}

@Preview(showBackground = true, name = "Cliente – Dark")
@Composable
private fun ClientePreviewDark() {
    heytitoTheme(darkTheme = true, dynamicColor = false) {
        ClienteDashboardScreen(onFilterClick = {})
    }
}
