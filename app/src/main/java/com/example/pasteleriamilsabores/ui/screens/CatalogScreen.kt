package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleriamilsabores.R
import com.example.pasteleriamilsabores.model.Producto
import com.example.pasteleriamilsabores.viewmodel.CartViewModel
import com.example.pasteleriamilsabores.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(cartViewModel: CartViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas las categorías") }
    var expandedDropdown by remember { mutableStateOf(false) }

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
                        painter = painterResource(id = R.drawable.torta_chocolate),
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
        val productos = listOf(
            Producto(1, "Torta de Chocolate", "Delicioso postre artesanal con ingredientes naturales.", 12990, R.drawable.torta_chocolate),
            Producto(2, "Kuchen de Manzana", "Delicioso postre artesanal con ingredientes naturales.", 10990, R.drawable.kuchen_manzana),
            Producto(3, "Pie de Limón", "Delicioso postre artesanal con ingredientes naturales.", 9990, R.drawable.pie_limon)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Banner carousel
            item {
                BannerCarousel()
            }

            // Categorías circulares
            item {
                CategoriesSection()
            }

            // Dropdown de filtro de categorías
            item {
                CategoryFilterDropdown(
                    selectedCategory = selectedCategory,
                    expanded = expandedDropdown,
                    onExpandChange = { expandedDropdown = it },
                    onCategorySelect = {
                        selectedCategory = it
                        expandedDropdown = false
                    }
                )
            }

            // Sección de descuentos
            item {
                DiscountSection()
            }

            // Título de productos
            item {
                Text(
                    "Categoría Destacada",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Chocolate,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Productos en grid
            items(productos.size) { index ->
                val product = productos[index]
                ProductCard(
                    name = product.nombre,
                    description = product.descripcion,
                    price = "${'$'}${product.precio}",
                    imageRes = product.imagen
                ) {
                    cartViewModel.addToCart(product)
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
        // Simulación de banner
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = BlancoPuro
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "🍰",
                        fontSize = 40.sp
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "BODAS",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlancoPuro
            )
            Text(
                "FESTIVO",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BlancoPuro
            )
            Text(
                "CUMPLEAÑOS",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlancoPuro
            )
        }

        // Indicador de página
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pageCount) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            if (index == currentPage) BlancoPuro else BlancoPuro.copy(alpha = 0.5f),
                            CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun CategoriesSection() {
    val categories = listOf(
        "Alcohol/\nCerveza" to "🍺",
        "Boda" to "💒",
        "Niña" to "👧",
        "Niño" to "👦"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        categories.forEach { (name, emoji) ->
            CategoryItem(name, emoji)
        }
    }
}

@Composable
fun CategoryItem(name: String, emoji: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { /* Navegar a categoría */ }
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = BlancoPuro,
            shadowElevation = 6.dp,
            border = androidx.compose.foundation.BorderStroke(2.dp, GrisSuave)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(emoji, fontSize = 32.sp)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            name,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            color = Chocolate,
            maxLines = 2,
            lineHeight = 12.sp
        )
    }
}

@Composable
fun CategoryFilterDropdown(
    selectedCategory: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onCategorySelect: (String) -> Unit
) {
    val categories = listOf(
        "Todas las categorías",
        "Tortas Cuadradas",
        "Tortas Circulares",
        "Postres Individuales",
        "Vegana",
        "Sin Gluten"
    )

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
            border = androidx.compose.foundation.BorderStroke(1.dp, GrisSuave)
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
fun ProductCard(name: String, description: String, price: String, imageRes: Int, onAdd: () -> Unit) {
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
                .padding(8.dp),
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
