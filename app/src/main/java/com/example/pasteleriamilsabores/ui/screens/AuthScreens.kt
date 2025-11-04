package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, authViewModel: com.example.pasteleriamilsabores.viewmodel.AuthViewModel) {
    // Crear el ViewModel dentro del cuerpo composable (no en la firma)
    // Especificamos el tipo genérico en la llamada a viewModel para evitar problemas de inferencia

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var loginAttempted by remember { mutableStateOf(false) }
    val loggedIn by authViewModel.sesionActiva.collectAsState()

    // Navegar cuando el intento de login haya sido exitoso y DataStore confirme la sesión
    LaunchedEffect(loginAttempted, loggedIn) {
        if (loginAttempted && loggedIn) {
            snackbarHostState.showSnackbar("Sesión iniciada")
            navController.navigate("catalog") {
                popUpTo("auth/login") { inclusive = true }
                launchSingleTop = true
            }
            loginAttempted = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Iniciar sesión", modifier = Modifier.padding(vertical = 12.dp), style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

        Button(onClick = {
            scope.launch {
                val ok = authViewModel.intentarLogin(email, password)
                if (ok) {
                    // marcamos que se intentó login; la navegación ocurrirá al observar sesionActiva
                    loginAttempted = true
                } else {
                    snackbarHostState.showSnackbar("Correo o contraseña inválidos.")
                }
            }
        }, modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("auth/register") }, modifier = Modifier.fillMaxWidth()) {
            Text("Registrarme")
        }

        Spacer(modifier = Modifier.height(12.dp))
        SnackbarHost(hostState = snackbarHostState)
    }
}
