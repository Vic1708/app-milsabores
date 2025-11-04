package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.pasteleriamilsabores.viewmodel.ProductsViewModel
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.model.Producto
import com.example.pasteleriamilsabores.ui.theme.*

/**
 * ProductosScreen (detalle de producto)
 * Parámetros:
 * - navController: para navegación (regresar)
 * - productId: id del producto a mostrar (nullable)
 *
 * Si el producto no existe, muestra un mensaje.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(navController: NavController, productId: Int?) {
    val context = LocalContext.current
    val productsVm: ProductsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.pasteleriamilsabores.viewmodel.ProductsViewModel.Factory(context.applicationContext as android.app.Application)
    )

    val productosList by productsVm.productos.collectAsState()
    val product = remember(productId, productosList) { productosList.find { it.id == productId } }

    // Crear CartViewModel con Factory para evitar crash al usar AndroidViewModel
    val cartViewModel: CartViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.pasteleriamilsabores.viewmodel.CartViewModel.Factory(context.applicationContext as android.app.Application)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(product?.nombre ?: "Producto", color = Chocolate) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // Volver a usar Icons.Filled.ArrowBack para compatibilidad y evitar import no soportado
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CremaClaro)
            )
        },
        containerColor = CremaClaro
    ) { padding ->
        if (product == null) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center) {
                Text("Producto no encontrado", color = MarronSuave)
            }
            return@Scaffold
        }

        var quantity by remember { mutableStateOf(1) }
        val totalPrice = product.precio * quantity

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoPuro)
            ) {
                Image(
                    painter = painterResource(id = product.imagen),
                    contentDescription = product.nombre,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            // Info
            Text(product.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Chocolate)
            Text(product.descripcion, fontSize = 15.sp, color = MarronSuave)

            // Categoría y precio
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(product.categoria, color = MarronSuave)
                Text("${'$'}${product.precio}", fontWeight = FontWeight.Bold, color = Chocolate, fontSize = 18.sp)
            }

            // Selector de cantidad
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = { if (quantity > 1) quantity -= 1 }) {
                    Icon(imageVector = Icons.Filled.Remove, contentDescription = "menos")
                }
                Text(quantity.toString(), fontSize = 18.sp, modifier = Modifier.width(36.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                IconButton(onClick = { quantity += 1 }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "más")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Total: ${'$'}${"%.2f".format(totalPrice.toDouble())}", fontWeight = FontWeight.Bold, color = Chocolate)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Acciones
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { /* Favorito u otra acción */ }, modifier = Modifier.weight(1f)) {
                    Text("Favorito")
                }

                Button(onClick = {
                    // Añadir al carrito con cantidad
                    cartViewModel.addToCart(product, quantity)
                    // Navegar al carrito
                    navController.navigate("cart")
                }, modifier = Modifier.weight(2f), colors = ButtonDefaults.buttonColors(containerColor = NaranjaSuave, contentColor = BlancoPuro)) {
                    Text("Agregar al carrito — ${'$'}${totalPrice}")
                }
            }
        }
    }
}
