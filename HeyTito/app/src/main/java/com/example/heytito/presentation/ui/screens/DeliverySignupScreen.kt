package com.example.heytito.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.text.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliverySignupScreen(
    onSubmitClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    // ——— Estado mock para progreso:
    // botoncito de “Subir” se habilita solo cuando todo está completo
    var personalDocs by remember { mutableStateOf(0) }
    var vehicleDocs by remember { mutableStateOf(0) }
    var selfieDone by remember { mutableStateOf(false) }
    var bankAdded by remember { mutableStateOf(false) }

    val personalReq = 1
    val vehicleReq = 3
    val selfieReq = 1
    val bankReq = 1

    val allGood = personalDocs >= personalReq &&
            vehicleDocs >= vehicleReq &&
            selfieDone &&
            bankAdded

    Scaffold(
        containerColor = colors.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de Repartidor", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    // botoncito back — si esto es modal, cambiar por Close
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // menucito para ver requisitos / ayuda rápida
                    IconButton(onClick = { /* TODO: abrir ayuda */ }) {
                        Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Ayuda")
                    }
                }
            )
        },
        bottomBar = {
            // CTA anclado (no se pierde al hacer scroll) — color fuerte pero no chillón
            Surface(tonalElevation = 2.dp, color = colors.surface) {
                Column(Modifier.padding(16.dp)) {
                    Button(
                        onClick = onSubmitClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = allGood, // solo cuando todo ok
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (allGood) "Enviar solicitud" else "Completa los pasos",
                            fontWeight = FontWeight.Medium
                        )
                    }
                    // comentario: si quieres feedback rápido, meter Snackbar al fallar validación
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            /* ——— Banner superior — usar primaryContainer + onPrimaryContainer (contraste correcto) */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.primaryContainer),
                shape = RoundedCornerShape(12.dp) // redondeadito para que se sienta tarjeta
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // iconito decorativo (compás): “tu ruta”
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(colors.onPrimaryContainer.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            tint = colors.onPrimaryContainer
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Únete como Repartidor",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Completa tu perfil para comenzar a entregar",
                        fontSize = 14.sp,
                        color = colors.onPrimaryContainer.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            /* ——— Secciones (cada una clicable con “dropzone” suave) — color así de suave para no tapar texto */
            DocumentSection(
                icon = Icons.Default.Description,
                title = "Documentos Personales",
                subtitle = "Documento de identidad (MÁX 5MB)",
                actionText = "Subir Documentos",
                actionIcon = Icons.Default.Upload,
                uploaded = personalDocs,
                required = personalReq,
                onAction = {
                    // TODO: abrir file picker (ActivityResultContracts.GetContent / OpenMultipleDocuments)
                    personalDocs = kotlin.comparisons.minOf(personalDocs + 1, personalReq)
                }
            )

            Spacer(Modifier.height(12.dp))

            DocumentSection(
                icon = Icons.Default.DirectionsCar,
                title = "Información del Vehículo",
                subtitle = "SOAT, Licencia de tránsito, foto (MÁX 5MB)",
                actionText = "Subir Documentos",
                actionIcon = Icons.Default.Upload,
                uploaded = vehicleDocs,
                required = vehicleReq,
                onAction = { vehicleDocs = kotlin.comparisons.minOf(vehicleDocs + 1, vehicleReq) }
            )

            Spacer(Modifier.height(12.dp))

            DocumentSection(
                icon = Icons.Default.Security,
                title = "Verificación de Identidad",
                subtitle = "Pon luz frontal y fondo neutro",
                actionText = if (selfieDone) "Repetir Selfie" else "Selfie de Verificación",
                actionIcon = Icons.Default.CameraAlt,
                uploaded = if (selfieDone) 1 else 0,
                required = selfieReq,
                onAction = { selfieDone = true } // TODO: abrir cámara con ML kit liveness si quieres subir el nivel
            )

            Spacer(Modifier.height(12.dp))

            DocumentSection(
                icon = Icons.Default.AccountBalance,
                title = "Cuenta Bancaria",
                subtitle = "Para pagos semanales",
                actionText = if (bankAdded) "Cambiar Cuenta" else "Agregar Cuenta Bancaria",
                actionIcon = Icons.Default.Add,
                uploaded = if (bankAdded) 1 else 0,
                required = bankReq,
                onAction = { bankAdded = true } // TODO: abrir formulario con validación (tipo, número, titular, banco)
            )

            Spacer(Modifier.height(24.dp))

            // mini leyenda de estado (tipo estudiante para el profe)
            AssistLegendRow()
            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ───────────────────────── Componentes ───────────────────────── */

@Composable
private fun DocumentSection(
    icon: ImageVector,
    title: String,
    subtitle: String,
    actionText: String,
    actionIcon: ImageVector,
    uploaded: Int,
    required: Int,
    onAction: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val done = uploaded >= required
    val progress = if (required == 0) 1f else (uploaded.toFloat() / required.toFloat())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAction() }, // tap en toda la tarjeta — UX rápido
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = colors.primary)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                    if (subtitle.isNotEmpty()) {
                        Spacer(Modifier.height(2.dp))
                        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                    }
                }
                // Chipito de estado: “0/1”, “2/3”
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = if (done) colors.primary.copy(alpha = 0.15f) else colors.surfaceVariant,
                    border = if (done) null else BorderStroke(1.dp, colors.outline.copy(alpha = 0.4f))
                ) {
                    Text(
                        "$uploaded/$required",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (done) colors.primary else colors.onSurface
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Dropzone / acción — color suave para no “gritar”
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.surfaceVariant)
                    .clickable { onAction() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(actionIcon, contentDescription = null, tint = colors.onSurfaceVariant)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        actionText,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                    // comentario: aquí metería un subtítulo chiquito tipo “PDF/JPG/PNG máx 5MB”
                }
            }

            Spacer(Modifier.height(12.dp))

            // Barra de progreso chiquita (para que el profe vea que hay feedback, jeje)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(999.dp)),
                trackColor = colors.surfaceVariant,
                color = if (done) colors.primary else colors.primary.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
private fun AssistLegendRow() {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // puntico verde “listo”
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(colors.primary)
        )
        Text(
            "Listo",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant) // color neutro
        )
        Text(
            "Pendiente",
            style = MaterialTheme.typography.bodySmall
        )
        // comentario: esto es puramente visual; si quieres a11y full, usar contentDescription en chips
    }
}


@Preview(showBackground = true, showSystemUi = true, name = "DeliverySignup – Theme Preview")
@Composable
private fun DeliverySignupPreview() {
    com.example.compose.heytitoTheme {
        DeliverySignupScreen()
    }
}