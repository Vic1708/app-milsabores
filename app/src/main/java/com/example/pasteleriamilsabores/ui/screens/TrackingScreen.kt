package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.animation.core.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleriamilsabores.ui.theme.*
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen() {
    val context = LocalContext.current
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModel.Factory(context.applicationContext as android.app.Application)
    )

    val estados = listOf(
        "Pendiente 📝" to 0,
        "En preparación 👨‍🍳" to 1,
        "En camino 🚚" to 2,
        "Entregado ✅" to 3
    )

    var estadoActual by remember { mutableStateOf(0) }
    var pedidoNumber by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(0.0) }
    var fechaEntrega by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }

    // Cargar el pedido más reciente
    LaunchedEffect(Unit) {
        try {
            val order = cartViewModel.obtenerOrdenReciente()
            if (order != null) {
                pedidoNumber = order.orderNumber
                direccion = order.direccion
                comuna = order.comuna
                total = order.total
                fechaEntrega = order.fechaEntrega
                telefono = order.telefono
                estadoActual = order.progreso
            }
        } catch (e: Exception) {
            pedidoNumber = "Error al cargar"
        } finally {
            cargando = false
        }
    }

    // Animación de progreso automático solo si aún no está entregado
    LaunchedEffect(estadoActual) {
        if (estadoActual < estados.lastIndex) {
            delay(3000L)
            val nuevoEstado = estadoActual + 1
            estadoActual = nuevoEstado

            // Actualizar el estado en BD
            try {
                val order = cartViewModel.obtenerOrdenReciente()
                if (order != null) {
                    val updatedOrder = order.copy(
                        progreso = nuevoEstado,
                        estado = estados[nuevoEstado].first
                    )
                    cartViewModel.actualizarOrden(updatedOrder)
                }
            } catch (e: Exception) {
                // Ignorar errores de actualización
            }
        }
    }

    val progreso: Float = (estadoActual + 1) / estados.size.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progreso,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOutCubic),
        label = "progress"
    )

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
                            "Seguimiento 🚚",
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
        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = VerdePastel)
            }
        } else if (pedidoNumber.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No hay pedidos activos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MarronSuave
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 📦 Card del número de pedido
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoPuro),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Número de pedido",
                            style = MaterialTheme.typography.labelMedium,
                            color = MarronSuave,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            pedidoNumber,
                            fontSize = 18.sp,
                            color = Chocolate,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 🔄 Card del estado actual
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoPuro),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Estado actual:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MarronSuave,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = estados[estadoActual].first,
                            fontSize = 24.sp,
                            color = Chocolate,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp),
                            color = if (estadoActual == estados.lastIndex) VerdeOscuro else NaranjaOscuro,
                            trackColor = GrisSuave
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "${(animatedProgress * 100).toInt()}% completado",
                            fontSize = 14.sp,
                            color = MarronSuave,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // 📍 Card de información de entrega
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GrisSuave.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Dirección:", fontWeight = FontWeight.Medium, color = Chocolate)
                            Text(direccion, color = MarronSuave, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Comuna:", fontWeight = FontWeight.Medium, color = Chocolate)
                            Text(comuna, color = MarronSuave, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Fecha de entrega:", fontWeight = FontWeight.Medium, color = Chocolate)
                            Text(fechaEntrega, color = MarronSuave, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Teléfono:", fontWeight = FontWeight.Medium, color = Chocolate)
                            Text(telefono, color = MarronSuave, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", fontWeight = FontWeight.Bold, color = Chocolate, fontSize = 16.sp)
                            Text("$${"%.2f".format(total)}", fontWeight = FontWeight.Bold, color = NaranjaOscuro, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

