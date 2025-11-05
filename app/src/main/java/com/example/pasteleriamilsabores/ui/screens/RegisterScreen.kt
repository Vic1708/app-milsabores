package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
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
    var rut by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val registerResult by userViewModel.registerResult.collectAsState()

    // Material3: usar SnackbarHostState en lugar de rememberScaffoldState
    val snackbarHostState = remember { SnackbarHostState() }

    // Errores inline
    var nombreError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var fechaError by remember { mutableStateOf<String?>(null) }
    var direccionError by remember { mutableStateOf<String?>(null) }
    var rutError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(registerResult) {
        when (registerResult) {
            is RegisterResult.Success -> {
                snackbarHostState.showSnackbar("Cuenta creada")
                userViewModel.clearRegisterResult()
                navController.navigate("auth/login") {
                    popUpTo("auth/register") { inclusive = true }
                }
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
        val scrollState = rememberScrollState()
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(padding)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Avatar de ejemplo
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.brownie), contentDescription = "Avatar",
                    modifier = Modifier.size(88.dp).clip(RoundedCornerShape(44.dp)))
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    val palabras = it.trim().split("\\s+".toRegex())
                    nombreError = when {
                        it.isBlank() -> "Ingresa tu nombre completo"
                        it.trim().length <= 3 -> "El nombre debe tener más de 3 caracteres"
                        palabras.size < 2 -> "Ingresa nombre y apellido"
                        else -> null
                    }
                },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nombreError != null
            )
            if (nombreError != null) Text(nombreError!!, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = when {
                        it.isBlank() -> "Ingresa un correo"
                        !it.contains("@") -> "El correo debe contener @"
                        else -> null
                    }
                },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null
            )
            if (emailError != null) Text(emailError!!, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = when {
                        it.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                        else -> null
                    }
                    confirmPasswordError = if (confirmPassword.isNotEmpty() && confirmPassword != it) "Las contraseñas no coinciden" else null
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "Mostrar")
                    }
                },
                isError = passwordError != null
            )
            if (passwordError != null) Text(passwordError!!, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = when {
                        it.isBlank() -> "Confirma la contraseña"
                        it != password -> "Las contraseñas no coinciden"
                        else -> null
                    }
                },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "Mostrar")
                    }
                },
                isError = confirmPasswordError != null
            )
            if (confirmPasswordError != null) Text(confirmPasswordError!!, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = {
                    fechaNacimiento = it
                    fechaError = when {
                        it.isBlank() -> null
                        !it.matches(Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d{2}")) -> "Formato fecha: DD/MM/AAAA"
                        else -> null
                    }
                },
                label = { Text("Fecha de nacimiento (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = fechaError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if (fechaError != null) Text(fechaError!!, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = direccion,
                onValueChange = {
                    direccion = it
                    direccionError = if (it.isBlank()) null else null
                },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = rut,
                onValueChange = {
                    // permitir solo dígitos
                    val digits = it.filter { ch -> ch.isDigit() }
                    rut = digits
                    rutError = when {
                        rut.isBlank() -> ""
                        !rut.matches(Regex("^\\d{9}")) -> "RUT debe tener 9 dígitos sin puntos ni guion"
                        else -> null
                    }
                },
                label = { Text("RUT (9 dígitos, sin puntos ni guion)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = rutError != null
            )
            if (rutError != null && rutError!!.isNotBlank()) Text(rutError!!, color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    val digits = it.filter { ch -> ch.isDigit() }
                    telefono = digits
                    telefonoError = null
                },
                label = { Text("Teléfono (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            if (telefonoError != null) Text(telefonoError!!, color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            val isFormValid = nombreError == null && emailError == null && passwordError == null && confirmPasswordError == null && rutError == null && nombre.isNotBlank() && email.isNotBlank() && password.length >= 6 && confirmPassword == password

            Button(onClick = {
                userViewModel.registerUser(nombre, email, password, confirmPassword, fechaNacimiento, direccion, rut, telefono)
            }, enabled = isFormValid, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = NaranjaSuave, contentColor = BlancoPuro)) {
                Text("Aceptar")
            }
        }
    }
}
