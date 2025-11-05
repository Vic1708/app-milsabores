package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.model.Producto
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.ui.theme.*
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModelParam: CartViewModel? = null) {

    val context = LocalContext.current
    val cartVm: CartViewModel = viewModelParam ?: androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.pasteleriamilsabores.viewmodel.CartViewModel.Factory(context.applicationContext as android.app.Application)
    )

    // Recolectar entidades del carrito (tiene quantity)
    val cartEntities by cartVm.cartItems.collectAsState()
    // Recolectar lista UI-friendly (Producto) para mostrar
    val cartItems by cartVm.cartAsProducto.collectAsState()

    // Calcular total reactivamente a partir de entidades (precio * cantidad)
    val total by remember(cartEntities) { derivedStateOf { cartEntities.sumOf { it.precio * it.quantity } } }

    // Calcular cantidad total de unidades en el carrito
    val itemCount by remember(cartEntities) { derivedStateOf { cartEntities.sumOf { it.quantity } } }

    // Obtener estado de sesión
    val authVm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = AuthViewModel.Factory(context)
    )
    val loggedIn by authVm.sesionActiva.collectAsState(initial = false)

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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Mostrar solo el icono del carrito en la AppBar (sin badges)
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = BlancoPuro,
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                "Carrito",
                                color = BlancoPuro,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
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
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "El carrito está vacío 🥺",
                        fontSize = 18.sp,
                        color = MarronSuave,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                // 📦 Lista de productos en el carrito
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { producto: Producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = BlancoPuro),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        producto.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Chocolate
                                    )
                                    Text(
                                        "$${producto.precio}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MarronSuave,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                IconButton(
                                    onClick = { cartVm.removeFromCart(producto) },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = NaranjaOscuro
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar"
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🧾 Total
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GrisSuave)
                ) {
                    Text(
                        "Total: $${"%.2f".format(total)}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Chocolate
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🧭 Botones de acción
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { cartVm.clearCart() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = NaranjaOscuro
                        )
                    ) {
                        Text("Vaciar", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (loggedIn) {
                                navController.navigate("checkout")
                            } else {
                                navController.navigate("auth/login")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NaranjaSuave,
                            contentColor = BlancoPuro
                        )
                    ) {
                        Text("Checkout", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
