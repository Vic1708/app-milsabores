package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CheckoutScreen(navController: NavController, viewModelParam: CartViewModel? = null) {
    val context = LocalContext.current
    val viewModel: CartViewModel = viewModelParam ?: androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.pasteleriamilsabores.viewmodel.CartViewModel.Factory(context.applicationContext as android.app.Application)
    )

    var direccion by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var fechaEntrega by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var mensaje by remember { mutableStateOf("") }
    var mensajeTipo by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var pedidoNumber by remember { mutableStateOf("") }

    val total = viewModel.calcularTotal()
    val scope = rememberCoroutineScope()

    // 🔹 Validación del formulario
    fun validarFormulario() {
        when {
            direccion.isBlank() || comuna.isBlank() || fechaEntrega.isBlank() || telefono.isBlank() -> {
                mensaje = "⚠️ Debes completar todos los campos."
                mensajeTipo = "error"
            }

            !fechaEntrega.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$")) -> {
                mensaje = "❌ Formato de fecha inválido (usa DD/MM/AAAA)."
                mensajeTipo = "error"
            }

            !validarFechaEntrega(fechaEntrega) -> {
                mensaje = "❌ La fecha debe ser posterior a hoy y no exceder los 20 días."
                mensajeTipo = "error"
            }

            telefono.length < 8 -> {
                mensaje = "📞 El teléfono ingresado no es válido."
                mensajeTipo = "error"
            }

            else -> {
                // 📦 Crear pedido
                isProcessing = true
                scope.launch {
                    try {
                        val order = viewModel.createOrder(
                            direccion = direccion,
                            comuna = comuna,
                            fechaEntrega = fechaEntrega,
                            telefono = telefono
                        )

                        if (order != null) {
                            pedidoNumber = order.orderNumber
                            mensaje = "✅ Pedido ${order.orderNumber} confirmado. Total: $${"%.2f".format(total)}"
                            mensajeTipo = "success"

                            // Limpiar carrito y formulario
                            viewModel.clearCart()
                            direccion = ""
                            comuna = ""
                            fechaEntrega = ""
                            telefono = ""

                            // Navegar a Seguimiento después de 1.5 segundos
                            delay(1500)
                            android.util.Log.d("CheckoutScreen", "Navegando a tracking...")
                            navController.navigate("tracking") {
                                popUpTo("checkout") { inclusive = true }
                            }
                        } else {
                            mensaje = "❌ Error al crear el pedido."
                            mensajeTipo = "error"
                        }
                    } catch (e: Exception) {
                        mensaje = "❌ Error: ${e.message}"
                        mensajeTipo = "error"
                    } finally {
                        isProcessing = false
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = CremaClaro,
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                color = VerdePastel,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Checkout 📦",
                            color = BlancoPuro,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = VerdePastel,
                        titleContentColor = BlancoPuro
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Información de Envío",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Chocolate
            )

            // 🏠 Dirección
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdePastel,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = VerdePastel
                )
            )

            // 🏘️ Comuna
            OutlinedTextField(
                value = comuna,
                onValueChange = { comuna = it },
                label = { Text("Comuna") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdePastel,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = VerdePastel
                )
            )

            // 📅 Fecha de entrega
            OutlinedTextField(
                value = fechaEntrega,
                onValueChange = { fechaEntrega = it },
                label = { Text("Fecha de entrega (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdePastel,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = VerdePastel
                )
            )

            // ☎️ Teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdePastel,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = VerdePastel
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🧾 Confirmar pedido
            Button(
                onClick = { validarFormulario() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isProcessing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NaranjaSuave,
                    contentColor = BlancoPuro,
                    disabledContainerColor = NaranjaSuave.copy(alpha = 0.6f)
                )
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp),
                        color = BlancoPuro,
                        strokeWidth = 2.dp
                    )
                }
                Text(
                    if (isProcessing) "Procesando..." else "Confirmar pedido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 💬 Mensaje animado
            AnimatedVisibility(
                visible = mensaje.isNotEmpty(),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (mensajeTipo == "success")
                            GrisSuave.copy(alpha = 0.4f)
                        else
                            NaranjaOscuro.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        mensaje,
                        modifier = Modifier.padding(16.dp),
                        color = if (mensajeTipo == "success") VerdeOscuro else NaranjaOscuro,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private fun validarFechaEntrega(fechaStr: String): Boolean {
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formato.isLenient = false
    return try {
        val fechaEntregaDate = formato.parse(fechaStr) ?: return false

        // --- HOY (inicio del día) ---
        val hoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // --- LÍMITE (20 días desde hoy) ---
        val maximo = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 20)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        // --- FECHA DE ENTREGA (a comparar) ---
        val fechaEntregaCal = Calendar.getInstance().apply {
            time = fechaEntregaDate
        }

        // La fecha de entrega debe ser posterior a hoy y no exceder el límite
        fechaEntregaCal.after(hoy) && fechaEntregaCal.before(maximo)
    } catch (e: Exception) {
        false
    }
}
