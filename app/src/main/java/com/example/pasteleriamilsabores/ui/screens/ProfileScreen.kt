package com.example.pasteleriamilsabores.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.pasteleriamilsabores.ui.theme.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var showLoginDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = CremaClaro
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header con avatar y login
            item {
                ProfileHeader(onLoginClick = { showLoginDialog = true })
            }

            // Banner de bienvenida
            item {
                WelcomeBanner()
            }

            // Cupones
            item {
                CouponSection()
            }

            // Estados de pedidos
            item {
                OrderStatusSection()
            }

            // Opciones de menú
            item {
                MenuOptions()
            }
        }
    }

    if (showLoginDialog) {
        LoginDialog(onDismiss = { showLoginDialog = false })
    }
}

@Composable
fun ProfileHeader(onLoginClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        VerdePastel.copy(alpha = 0.3f),
                        CremaClaro
                    )
                )
            )
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = GrisSuave
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("👤", fontSize = 32.sp)
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            TextButton(onClick = onLoginClick) {
                Text(
                    "Haga clic para iniciar sesión",
                    fontSize = 16.sp,
                    color = Chocolate
                )
            }
        }

        IconButton(onClick = { /* Configuración */ }) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_preferences),
                contentDescription = "Configuración",
                tint = Chocolate
            )
        }
    }
}

@Composable
fun WelcomeBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Chocolate)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Pastelería Diseño Bienvenido",
                color = BlancoPuro,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = NaranjaSuave),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Información", color = Chocolate)
            }
        }
    }
}

@Composable
fun CouponSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BlancoPuro)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "0",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Chocolate
            )
            Text(
                "Cupones",
                fontSize = 16.sp,
                color = MarronSuave
            )
        }
    }
}

@Composable
fun OrderStatusSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OrderStatusItem("📋", "Todas")
        OrderStatusItem("⏳", "Procesando")
        OrderStatusItem("🚚", "En camino")
        OrderStatusItem("⭐", "Evaluar")
    }
}

@Composable
fun OrderStatusItem(icon: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = NaranjaSuave.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(icon, fontSize = 24.sp)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            label,
            fontSize = 12.sp,
            color = Chocolate,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun MenuOptions() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        MenuOptionItem("📍", "Gestión de direcciones")
        Divider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("🎫", "Cupones", subtitle = "Tienes 0 cupones sin usar")
        Divider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("🔒", "Política de privacidad")
        Divider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("🚚", "Política de entrega")
        Divider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("👥", "Sobre nosotros")
        Divider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("⭐", "Mi Favoritos")
        Divider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("⚙️", "Configurar")
    }
}

@Composable
fun MenuOptionItem(icon: String, title: String, subtitle: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    fontSize = 16.sp,
                    color = Chocolate
                )
                if (subtitle != null) {
                    Text(
                        subtitle,
                        fontSize = 12.sp,
                        color = MarronSuave
                    )
                }
            }
        }
        Text("›", fontSize = 24.sp, color = MarronSuave)
    }
}

@Composable
fun LoginDialog(onDismiss: () -> Unit) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BlancoPuro)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "¡Bienvenido de nuevo!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Chocolate
                )

                Spacer(Modifier.height(24.dp))

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email o número de móvil") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NaranjaSuave)
                ) {
                    Text(
                        "Iniciar sesión",
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 16.sp,
                        color = BlancoPuro
                    )
                }

                Spacer(Modifier.height(16.dp))

                TextButton(onClick = { }) {
                    Text("¿Aún no tienes una cuenta? Regístrate ahora", color = VerdePastel)
                }
            }
        }
    }
}
