package com.example.heytito.presentation.ui.screens.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heytito.R

// --------- Modelo ----------
data class ChatRow(
    val id: String,
    val title: String,          // nombre del contacto/tienda
    val subtitle: String,       // último mensaje
    val time: String? = null,   // "09:18", "Ayer", "23 Ago"
    val unread: Int = 0,
    val pinned: Boolean = false,
    val isTito: Boolean = false,
    @DrawableRes val avatarResId: Int? = null // 👈 avatar por drawable
)

// --------- Pantalla ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onBackClick: () -> Unit = {},
    onOpenChat: (chatId: String, isTito: Boolean) -> Unit = { _, _ -> },
    onNewChat: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    // Ejemplo de chats (avatares: slash, maluma, diomedes)
    val chats = remember {
        listOf(
            ChatRow("1","Atelier Nova","Claro, te paso medidas...", time = "09:18", unread = 3, avatarResId = R.drawable.slash),
            ChatRow("2","Laura P.","Recibí el pedido, gracias :)", time = "Ayer", avatarResId = R.drawable.maluma),
            ChatRow("3","Luna Urban","Hicimos el envío hoy", time = "23 Ago", unread = 1, avatarResId = R.drawable.shaki),
            ChatRow("4","Carlos D.","¿Tienen talla S en azul?", time = "22 Ago", avatarResId =  R.drawable.diomedes)
        )
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colors.surface,
                tonalElevation = 1.dp,
                shadowElevation = 1.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = colors.onSurface)
                    }
                    Text(
                        text = "Chats",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = colors.onSurface
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Row(modifier = Modifier.align(Alignment.CenterEnd), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onMoreClick) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Más", tint = colors.onSurface)
                        }
                    }
                }
            }
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp)
        ) {
            // Buscador
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Buscar chat o usuario…") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(Modifier.height(10.dp))

            // Chips de filtro
            FilterRow(
                options = listOf("Todos", "No leídos", "Tiendas"),
                selected = selectedFilter,
                onSelect = { selectedFilter = it }
            )

            Spacer(Modifier.height(8.dp))

            val tito = ChatRow(
                id = "tito",
                title = "Tito",
                subtitle = "¿Necesitas algún consejo?",
                pinned = true,
                isTito = true,
                avatarResId = R.drawable.tito // 👈 avatar de Tito
            )

            // Lista
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Card de Tito fijado
                item {
                    TitoPinnedCard(
                        row = tito,
                        onClick = { onOpenChat(tito.id, true) }
                    )
                }
                // Resto de chats
                items(chats) { row ->
                    ChatListCard(
                        row = row,
                        onClick = { onOpenChat(row.id, false) }
                    )
                }
                // Botón Nuevo chat al final
                item {
                    OutlinedButton(
                        onClick = onNewChat,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Nuevo chat", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// --------- Subcomposables ----------
@Composable
private fun FilterRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { opt ->
            FilterChip(
                selected = selected == opt,
                onClick = { onSelect(opt) },
                label = { Text(opt) }
            )
        }
    }
}

@Composable
private fun TitoPinnedCard(
    row: ChatRow,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        ListItem(
            leadingContent = {
                Box(
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    // Imagen de Tito
                    val avatar = row.avatarResId
                    if (avatar != null) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = avatar),
                            contentDescription = "Avatar Tito",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(colors.surfaceVariant)
                        )
                    }
                    // Punto de estado (online)
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(colors.primary)
                    )
                }
            },
            headlineContent = { Text(row.title, fontWeight = FontWeight.SemiBold) },
            supportingContent = { Text(row.subtitle) },
            trailingContent = {
                AssistChip(
                    onClick = onClick,
                    label = { Text("Fijado") },
                    leadingIcon = {
                        Icon(Icons.Default.PushPin, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                )
            }
        )
    }
}

@Composable
private fun ChatListCard(
    row: ChatRow,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        ListItem(
            leadingContent = {
                Box(
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    val avatar = row.avatarResId
                    if (avatar != null) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = avatar),
                            contentDescription = "Avatar de ${row.title}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(colors.surfaceVariant)
                        )
                    }
                }
            },
            headlineContent = { Text(row.title, fontWeight = FontWeight.SemiBold) },
            supportingContent = { Text(row.subtitle) },
            trailingContent = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    row.time?.let { Text(it, fontSize = 12.sp, color = colors.onSurfaceVariant) }
                    if (row.unread > 0) {
                        BadgedBox(badge = { Badge { Text("${row.unread}") } }) {
                            Spacer(Modifier.size(1.dp))
                        }
                    }
                }
            }
        )
    }
}
