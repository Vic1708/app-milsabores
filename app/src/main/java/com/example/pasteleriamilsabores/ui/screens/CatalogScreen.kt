package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pasteleriamilsabores.R
import com.example.pasteleriamilsabores.viewmodel.ProductsViewModel
import com.example.pasteleriamilsabores.model.Producto
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.util.Log
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navController: NavHostController, cartViewModelParam: CartViewModel? = null) {
    // Crear CartViewModel con Factory si no fue inyectado
    val context = LocalContext.current
    val cartViewModel: CartViewModel = cartViewModelParam ?: run {
        val factory = com.example.pasteleriamilsabores.viewmodel.CartViewModel.Factory(context.applicationContext as android.app.Application)
        androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
    }

    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas las categorías") }
    var expandedDropdown by remember { mutableStateOf(false) }

    // Usar ProductsViewModel para exponer productos desde Room y categorías
    val productsVm: ProductsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.pasteleriamilsabores.viewmodel.ProductsViewModel.Factory(context.applicationContext as android.app.Application)
    )

    val productosList by productsVm.productos.collectAsState()
    // Calcular categorías a partir de la lista actual de productos para reactividad
    val categories by remember(productosList) {
        derivedStateOf {
            val cats = productosList.map { it.categoria }.filterNotNull().distinct().sorted()
            listOf("Todas las categorías") + cats
        }
    }

    val productosMapped = remember(productosList) { productosList }

    Scaffold(
        containerColor = BlancoPuro,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NaranjaSuave)
            ) {
                // Barra superior
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_view),
                        contentDescription = "Escanear",
                        tint = BlancoPuro,
                        modifier = Modifier.size(28.dp)
                    )

                    Image(
                        // Usamos la imagen `brownie` para el logo según la petición
                        painter = painterResource(id = R.drawable.brownie),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }

                // Barra de búsqueda
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Nombre o Código del producto", color = MarronSuave) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_search),
                            contentDescription = "Buscar",
                            tint = MarronSuave
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = BlancoPuro,
                        unfocusedContainerColor = BlancoPuro,
                        focusedBorderColor = BlancoPuro,
                        unfocusedBorderColor = BlancoPuro
                    )
                )
            }
        }
    ) { padding ->
        // Filtrar la lista mapeada por categoría y búsqueda
        val productos = remember(searchText, selectedCategory, productosMapped) {
            val q = searchText.trim().lowercase()
            productosMapped.filter { p ->
                val matchesCategory = (selectedCategory == "Todas las categorías") || p.categoria == selectedCategory
                val matchesQuery = q.isEmpty() || p.nombre.lowercase().contains(q) || p.code.lowercase().contains(q)
                matchesCategory && matchesQuery
            }
        }

        // Debug visual: mostrar cuántos productos encontró el filtro
        val productosCount = productos.size
        LaunchedEffect(productosCount) {
            println("[CatalogScreen] productos encontrados: $productosCount, filtro='$selectedCategory', query='$searchText' (desde DB? ${productosList.isNotEmpty()})")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()), // 👈 necesario para hacerla desplazable
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Si no hay productos, mostrar mensaje informativo
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No se encontraron productos.", color = MarronSuave)
                        Spacer(Modifier.height(8.dp))
                        Text("Revisa los filtros o la base de datos.", color = MarronSuave)
                        Spacer(Modifier.height(12.dp))
                        Text("Debug: productos encontrados = $productosCount", color = MarronSuave)
                    }
                }
            }

            // Banner carousel
            BannerCarousel()

            // Categorías circulares (ahora horizontalmente desplazables)
            CategoriesSection(
                categories = categories,
                selected = selectedCategory,
                onSelect = { selectedCategory = it }
            )

            // Dropdown de filtro de categorías
            CategoryFilterDropdown(
                categories = categories,
                selectedCategory = selectedCategory,
                expanded = expandedDropdown,
                onExpandChange = { expandedDropdown = it },
                onCategorySelect = {
                    selectedCategory = it
                    expandedDropdown = false
                }
            )

            // Sección de descuentos
            DiscountSection()

            // Título de productos
            Text(
                "Categoría Destacada",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Chocolate,
                modifier = Modifier.padding(16.dp)
            )

            // Productos en grid (lista)
            productos.forEach { product ->
                ProductCard(
                    name = product.nombre,
                    description = product.descripcion,
                    price = "${'$'}${product.precio}",
                    imageRes = product.imagen,
                    onAdd = {
                        // Acción al agregar: usar el viewModel pasado
                        cartViewModel.addToCart(
                            Producto(
                                id = product.id,
                                code = product.code,
                                nombre = product.nombre,
                                descripcion = product.descripcion,
                                precio = product.precio,
                                imagen = product.imagen,
                                categoria = product.categoria
                            )
                        )
                    }
                ) {
                    // onClick (detalle): navegar a la pantalla de producto
                    navController.navigate("producto/${product.id}")
                }
            }
        }

    }
}

@Composable
fun BannerCarousel() {
    var currentPage by remember { mutableStateOf(0) }
    val pageCount = 3

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentPage = (currentPage + 1) % pageCount
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(NaranjaSuave)
    ) {
        // Usamos la función `AnimatedSlide` para renderizar dinámicamente el contenido del banner
        // AnimatedSlide sincroniza la animación de la imagen (brownie.png) y los textos
        AnimatedSlide(currentPage)

        // Indicador de página
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pageCount) { index ->
                // Animación por indicador: tamaño y color
                val targetSize by animateDpAsState(targetValue = if (index == currentPage) 12.dp else 8.dp, animationSpec = tween(300))
                val targetColor by animateColorAsState(targetValue = if (index == currentPage) BlancoPuro else BlancoPuro.copy(alpha = 0.5f), animationSpec = tween(300))

                Box(
                    modifier = Modifier
                        .size(targetSize)
                        .background(
                            color = targetColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun CategoriesSection(categories: List<String>, selected: String, onSelect: (String) -> Unit) {
    // Mapear emojis básicos según palabra clave (puedes adaptar a recursos icónicos)
    val emojiFor = mapOf(
        "Tortas Cuadradas" to "🎂",
        "Tortas Circulares" to "🍰",
        "Postres Individuales" to "🧁",
        "Vegana" to "🥦",
        "Sin Gluten" to "🌾",
        "Todas las categorías" to "🧁"
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { name ->
            val emoji = emojiFor[name] ?: "🧁"
            CategoryItem(name = name, emoji = emoji, selected = (name == selected), onClick = { onSelect(name) })
        }
        item { Spacer(modifier = Modifier.width(24.dp)) }
    }
}

@Composable
fun CategoryItem(name: String, emoji: String, selected: Boolean = false, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(92.dp)
            .clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(68.dp),
            shape = CircleShape,
            color = if (selected) VerdePastel else BlancoPuro,
            shadowElevation = if (selected) 8.dp else 4.dp,
            border = BorderStroke(2.dp, if (selected) VerdeOscuro else GrisSuave)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(emoji, fontSize = 28.sp)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            name,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Chocolate,
            maxLines = 2,
            lineHeight = 13.sp
        )
    }
}

@Composable
fun CategoryFilterDropdown(
    categories: List<String>,
    selectedCategory: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onCategorySelect: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandChange(!expanded) },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = BlancoPuro),
            border = BorderStroke(1.dp, GrisSuave)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedCategory,
                    fontSize = 14.sp,
                    color = Chocolate
                )
                Icon(
                    painter = painterResource(
                        id = if (expanded) android.R.drawable.arrow_up_float
                        else android.R.drawable.arrow_down_float
                    ),
                    contentDescription = "Expandir",
                    tint = Chocolate,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandChange(false) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(BlancoPuro)
        ) {
            // Usamos las categorías provistas por el caller (ProductsViewModel)
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            category,
                            fontSize = 14.sp,
                            color = if (category == selectedCategory) VerdePastel else Chocolate
                        )
                    },
                    onClick = { onCategorySelect(category) },
                    modifier = Modifier.background(
                        if (category == selectedCategory) GrisSuave else BlancoPuro
                    )
                )
            }
        }
    }
}

@Composable
fun DiscountSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GrisSuave)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Descuento",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NaranjaOscuro
            )
            Icon(
                painter = painterResource(id = android.R.drawable.arrow_down_float),
                contentDescription = "Ver descuentos",
                tint = NaranjaOscuro
            )
        }
    }
}

@Composable
fun ProductCard(name: String, description: String, price: String, imageRes: Int, onAdd: () -> Unit, onClick: () -> Unit = {}) {
    var visible by remember { mutableStateOf(false) }
    var buttonPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val buttonColor by animateColorAsState(
        targetValue = if (buttonPressed) VerdeOscuro else NaranjaSuave,
        animationSpec = tween(300),
        label = "buttonColor"
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = BlancoPuro)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Chocolate
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    description,
                    fontSize = 14.sp,
                    color = MarronSuave,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Chocolate
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        buttonPressed = true
                        onAdd()
                        scope.launch {
                            delay(300)
                            buttonPressed = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = BlancoPuro
                    )
                ) {
                    Text("Agregar al carrito 🛒", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
