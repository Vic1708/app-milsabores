package com.example.pasteleriamilsabores.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.ui.theme.*
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import android.content.pm.PackageManager
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, authVm: com.example.pasteleriamilsabores.viewmodel.AuthViewModel) {
    val context = LocalContext.current
    val usuario by authVm.usuario.collectAsState(initial = null)
    val sesionActiva by authVm.sesionActiva.collectAsState(initial = false)

    // Obtén avatar desde AuthViewModel
    val avatarUri by authVm.avatar.collectAsState(initial = null)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Launchers: cámara (preview) y galería
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            // Guardar bitmap como archivo temporal y guardar su path en DataStore
            val path = saveBitmapToCacheAndGetPath(context, bitmap)
            authVm.guardarAvatar(path)
            scope.launch { snackbarHostState.showSnackbar("Avatar actualizado") }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Guardar content URI string (content://...)
            authVm.guardarAvatar(it.toString())
            scope.launch { snackbarHostState.showSnackbar("Avatar actualizado") }
        }
    }

    // Permission launcher for CAMERA
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            takePictureLauncher.launch(null)
        } else {
            scope.launch { snackbarHostState.showSnackbar("Permiso de cámara denegado") }
        }
    }

    // Helper to handle take photo with permission check
    val onTakePhotoHandled: () -> Unit = {
        val permission = android.Manifest.permission.CAMERA
        val hasPermission = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            takePictureLauncher.launch(null)
        } else {
            requestCameraPermissionLauncher.launch(permission)
        }
    }

    Scaffold(
        containerColor = CremaClaro,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header con avatar y datos del usuario
            item {
                ProfileHeader(
                    usuarioNombre = usuario?.nombre,
                    usuarioEmail = usuario?.email,
                    loggedIn = sesionActiva,
                    avatarUri = avatarUri,
                    onPickFromGallery = { pickImageLauncher.launch("image/*") },
                    onTakePhoto = { onTakePhotoHandled() },
                    onLoginClick = { navController.navigate("auth/login") },
                    onLogout = {
                        authVm.logout()
                        // Después de cerrar sesión, navegar a login
                        navController.navigate("auth/login") {
                            popUpTo("catalog") { inclusive = false }
                        }
                        scope.launch { snackbarHostState.showSnackbar("Sesión cerrada") }
                    },
                    snackbarHostState = snackbarHostState
                )
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
}

@Composable
fun ProfileHeader(
    usuarioNombre: String?,
    usuarioEmail: String?,
    loggedIn: Boolean,
    avatarUri: String?,
    onPickFromGallery: () -> Unit,
    onTakePhoto: () -> Unit,
    onLoginClick: () -> Unit,
    onLogout: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    // Local state para mostrar diálogo de selección de avatar
    var showAvatarDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val avatarBitmap: Bitmap? = remember(avatarUri) { avatarUri?.let { loadBitmapFromUri(context, it) } }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
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
                if (avatarBitmap != null) {
                    Image(bitmap = avatarBitmap.asImageBitmap(), contentDescription = "Avatar", modifier = Modifier.fillMaxSize())
                } else {
                    // fallback: recurso local
                    Image(painter = painterResource(id = com.example.pasteleriamilsabores.R.drawable.brownie), contentDescription = "Avatar", modifier = Modifier.fillMaxSize())
                }
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            if (loggedIn && usuarioNombre != null) {
                Text(usuarioNombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Chocolate)
                Text(usuarioEmail ?: "", fontSize = 14.sp, color = MarronSuave)
            } else {
                TextButton(onClick = onLoginClick) {
                    Text(
                        "Haga clic para iniciar sesión",
                        fontSize = 16.sp,
                        color = Chocolate
                    )
                }
            }

            // Botón que abre diálogo nativo para elegir fuente del avatar
            Row(modifier = Modifier.padding(top = 8.dp)) {
                OutlinedButton(onClick = { showAvatarDialog = true }, shape = RoundedCornerShape(12.dp)) {
                    Text("Editar avatar", color = Chocolate)
                }
            }
        }

        if (loggedIn) {
            Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = NaranjaSuave)) {
                Text("Cerrar sesión", color = BlancoPuro)
            }
        } else {
            IconButton(onClick = { /* Configuración */ }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_preferences),
                    contentDescription = "Configuración",
                    tint = Chocolate
                )
            }
        }
    }

    // Diálogo de selección: Cámara o Galería
    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    // Acción por defecto: abrir galería
                    onPickFromGallery()
                    showAvatarDialog = false
                }) { Text("Galería") }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Abrir cámara
                    onTakePhoto()
                    showAvatarDialog = false
                }) { Text("Cámara") }
            },
            title = { Text("Seleccionar avatar") },
            text = { Text("Elija una opción para actualizar su avatar") }
        )
    }
}

// Helper para guardar bitmap en cache y devolver path (absolute path)
fun saveBitmapToCacheAndGetPath(context: Context, bitmap: Bitmap): String {
    val cacheDir = context.cacheDir
    val file = File(cacheDir, "avatar_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
    }
    return file.absolutePath
}

// Cargar Bitmap desde path (file) o content URI
fun loadBitmapFromUri(context: Context, uriString: String): Bitmap? {
    return try {
        if (uriString.startsWith("/")) {
            BitmapFactory.decodeFile(uriString)
        } else if (uriString.startsWith("file://")) {
            val path = Uri.parse(uriString).path
            if (path != null) BitmapFactory.decodeFile(path) else null
        } else {
            // Puede ser content://
            val uri = Uri.parse(uriString)
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        }
    } catch (e: Exception) {
        null
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
        HorizontalDivider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("🎫", "Cupones", subtitle = "Tienes 0 cupones sin usar")
        HorizontalDivider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("🔒", "Política de privacidad")
        HorizontalDivider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("🚚", "Política de entrega")
        HorizontalDivider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("👥", "Sobre nosotros")
        HorizontalDivider(color = GrisSuave, thickness = 1.dp)

        MenuOptionItem("⭐", "Mi Favoritos")
        HorizontalDivider(color = GrisSuave, thickness = 1.dp)

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
