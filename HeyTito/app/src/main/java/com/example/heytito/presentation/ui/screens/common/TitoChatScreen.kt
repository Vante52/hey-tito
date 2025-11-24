package com.example.heytito.presentation.ui.screens.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image as FImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heytito.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitoChatScreen(
    onBackClick: () -> Unit = {},
    onViewProduct: () -> Unit = {},
    onViewProfile: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            // Header unificado
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colors.surface,
                tonalElevation = 1.dp,
                shadowElevation = 1.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Tito",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 22.sp,
                                color = colors.onSurface
                            )
                        )
                        Text(
                            "Online",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.primary,
                            fontSize = 12.sp
                        )
                    }

                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        IconButton(onClick = { /* menú */ }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ✅ MANTIENE el placeholder (mismo marco), pero muestra la imagen "camisa"
            RoundedImagePlaceholder(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(180.dp),
                resId = R.drawable.camisa
            )

            // Mensaje del usuario
            UserBubble("¿Sabes dónde puedo conseguir algo similar?")

            // Respuesta de Tito (texto)
            TitoBubble("Pues claro, ¡qué chimba de prenda! 😎")

            // ✅ MANTIENE el placeholder (mismo marco), con la imagen "encontrada"
            TitoImageMessage(
                resId = R.drawable.encontrada,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            )

            // Tarjeta de recomendación (mini preview con la misma imagen "encontrada")
            RecommendationCard(
                title = "Camisa Grid Roja",
                handle = "@urbanx",
                price = "$129.900",
                rating = "4.7",
                onViewProduct = onViewProduct,
                onViewProfile = onViewProfile
            )

            Spacer(Modifier.weight(1f))

            // Input
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Escribe un mensaje…") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                trailingIcon = {
                    IconButton(onClick = { /* enviar */ }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
                    }
                }
            )
        }
    }
}

/* ---------- PLACEHOLDER CON OPCIONAL IMAGEN (MISMO ESTILO DE ANTES) ---------- */

@Composable
private fun RoundedImagePlaceholder(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int? = null,                  // si viene, se muestra la imagen dentro del mismo marco
    contentScale: ContentScale = ContentScale.Crop
) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (resId != null) {
            FImage(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(16.dp)),   // respeta esquinas
                contentScale = contentScale
            )
        } else {
            Icon(
                Icons.Filled.Image,                    // icono (como antes)
                contentDescription = null,
                tint = colors.onSurfaceVariant
            )
        }
    }
}

/* ---------- Burbujas ---------- */

@Composable
private fun UserBubble(text: String) {
    val colors = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Surface(
            color = colors.surface,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 2.dp
        ) { Text(text, Modifier.padding(12.dp)) }
    }
}

@Composable
private fun TitoBubble(text: String) {
    val colors = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth()) {
        Surface(
            color = colors.primary.copy(alpha = .10f),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 0.dp
        ) { Text(text, Modifier.padding(12.dp)) }
    }
}

/* ---------- Mensaje visual de Tito reutilizando el mismo placeholder ---------- */

@Composable
private fun TitoImageMessage(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier
) {
    Row(Modifier.fillMaxWidth()) {
        RoundedImagePlaceholder(
            resId = resId,
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))       // mismo marco
        )
    }
}

/* ---------- Tarjeta de recomendación (mini preview mantiene el placeholder) ---------- */

@Composable
private fun RecommendationCard(
    title: String,
    handle: String,
    price: String,
    rating: String,
    onViewProduct: () -> Unit,
    onViewProfile: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // ✅ mini preview dentro del mismo placeholder, con "encontrada"
                RoundedImagePlaceholder(
                    resId = R.drawable.encontrada,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.SemiBold)
                    Text(handle, color = colors.onSurfaceVariant, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(price, fontWeight = FontWeight.Bold)
            AssistChip(
                onClick = {},
                label = { Text("$rating ★") },
                modifier = Modifier.padding(top = 4.dp)
            )
            Text("Creo que esta te quedará brutal 🔥", modifier = Modifier.padding(vertical = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onViewProduct,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Ver producto") }
                OutlinedButton(
                    onClick = onViewProfile,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Ver perfil") }
            }
        }
    }
}
