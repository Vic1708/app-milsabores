package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.R
import com.example.pasteleriamilsabores.ui.theme.*
import com.example.pasteleriamilsabores.viewmodel.RegisterResult
import com.example.pasteleriamilsabores.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    // Crear el ViewModel dentro del composable
    val userViewModel: UserViewModel = viewModel()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val registerResult by userViewModel.registerResult.collectAsState()

    // Material3: usar SnackbarHostState en lugar de rememberScaffoldState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerResult) {
        when (registerResult) {
            is RegisterResult.Success -> {
                snackbarHostState.showSnackbar("Cuenta creada")
                userViewModel.clearRegisterResult()
                navController.navigate("login") { popUpTo("register") { inclusive = true } }
            }
            is RegisterResult.AlreadyExists -> {
                snackbarHostState.showSnackbar("La cuenta ya existe")
                userViewModel.clearRegisterResult()
            }
            is RegisterResult.Error -> {
                val msg = (registerResult as RegisterResult.Error).message
                snackbarHostState.showSnackbar(msg)
                userViewModel.clearRegisterResult()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registrar cuenta", color = Chocolate) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CremaClaro)
            )
        },
        containerColor = CremaClaro,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Avatar de ejemplo
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.brownie), contentDescription = "Avatar",
                    modifier = Modifier.size(88.dp).clip(RoundedCornerShape(44.dp)))
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "Mostrar")
                    }
                }
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha de nacimiento (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            val isFormValid = nombre.isNotBlank() && email.isNotBlank() && password.length >= 6 && password == confirmPassword

            Button(onClick = {
                userViewModel.registerUser(nombre, email, password, confirmPassword, fechaNacimiento, direccion)
            }, enabled = isFormValid, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = NaranjaSuave, contentColor = BlancoPuro)) {
                Text("Aceptar")
            }
        }
    }
}
