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
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(viewModel: CartViewModel = viewModel()) {
    var direccion by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var fechaEntrega by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mensajeTipo by remember { mutableStateOf("") }

    val total = viewModel.calcularTotal()

    fun validarFormulario() {
        if (direccion.isBlank() || comuna.isBlank() || fechaEntrega.isBlank() || telefono.isBlank()) {
            mensaje = "⚠️ Debes completar todos los campos."
            mensajeTipo = "error"
            return
        }
        val fechaRegex = Regex("\\d{4}-\\d{2}-\\d{2}")
        if (!fechaEntrega.matches(fechaRegex)) {
            mensaje = "❌ Formato de fecha inválido (usa AAAA-MM-DD)."
            mensajeTipo = "error"
            return
        }
        mensaje = "✅ Pedido confirmado. Total a pagar: $${"%.2f".format(total.toDouble())}"
        mensajeTipo = "success"
        viewModel.clearCart()
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

            OutlinedTextField(
                value = fechaEntrega,
                onValueChange = { fechaEntrega = it },
                label = { Text("Fecha de entrega (AAAA-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdePastel,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = VerdePastel
                )
            )

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

            Button(
                onClick = { validarFormulario() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NaranjaSuave,
                    contentColor = BlancoPuro
                )
            ) {
                Text("Confirmar pedido", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = mensaje.isNotEmpty(),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (mensajeTipo == "success") GrisSuave else NaranjaOscuro.copy(alpha = 0.1f)
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
