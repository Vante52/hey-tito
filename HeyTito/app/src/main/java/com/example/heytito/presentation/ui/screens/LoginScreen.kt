package com.example.heytito.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.heytitoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    // usar saveable para rotación / proceso muerto
    var emailOrPhone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    // scaffold para poder meter topbar si luego queremos
    Scaffold(
        containerColor = colors.background,
        topBar = {
            // Top bar simple con título centrado
            Surface(color = colors.surface, tonalElevation = 1.dp, shadowElevation = 1.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // botoncito de volver (dejar simple)
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = colors.onSurface
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Iniciar sesión",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onSurface
                        )
                    )
                    // hueco para balancear el back (así el título queda bien centrado)
                    Spacer(Modifier.width(48.dp))
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(inner)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // texto de apoyo con onSurfaceVariant para que no “grite”
            Text(
                text = "Ingresa a tu cuenta y encuentra nuevas ofertas",
                style = MaterialTheme.typography.bodyMedium.copy(color = colors.onSurfaceVariant),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp)) // separador para que respire

            /* ------------------------ Email / Teléfono ------------------------ */
            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = { emailOrPhone = it },
                label = { Text("Introduce tu cuenta") },
                placeholder = { Text("Correo o número de teléfono", color = colors.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                // color así de suave para que no tape el contenido
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    cursorColor = colors.primary,
                    focusedLabelColor = colors.primary,
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(16.dp))

            /* ----------------------------- Password --------------------------- */
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                placeholder = { Text("Mínimo 8 caracteres", color = colors.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    // botoncito para mostrar/ocultar (UX rápido)
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = colors.onSurfaceVariant
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    cursorColor = colors.primary,
                    focusedLabelColor = colors.primary,
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(8.dp))

            // linkcito “olvidé mi pass” con primary (que destaque pero sin ser muy fuerte)
            Text(
                text = "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.labelLarge.copy(color = colors.primary),
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { onForgotPasswordClick() }
                    .padding(vertical = 8.dp)
            )

            Spacer(Modifier.weight(1f))

            // Botón principal: usar onPrimary para contraste AA
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = emailOrPhone.isNotBlank() && password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.surfaceVariant,
                    disabledContentColor = colors.onSurfaceVariant
                ),
                shape = MaterialTheme.shapes.large // redondeado bonito
            ) {
                Text(
                    text = "Continuar",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)
                )
            }

            Spacer(Modifier.height(24.dp))

            // TODO(uni): validar formato email/teléfono y mostrar supportingText si hay error
            // TODO(uni): botón social (Google/Apple) abajo “botoncito opcional”
            // TODO(uni): ajustar insets de IME si el teclado tapa el botón (WindowInsets.ime)
        }
    }
}

//Con el heytitoTheme

@Preview(showBackground = true, showSystemUi = true, name = "Login – Light")
@Composable
private fun LoginScreenPreviewLight() {
    heytitoTheme(darkTheme = false, dynamicColor = false) {
        LoginScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login – Dark")
@Composable
private fun LoginScreenPreviewDark() {
    heytitoTheme(darkTheme = true, dynamicColor = false) {
        LoginScreen()
    }
}
