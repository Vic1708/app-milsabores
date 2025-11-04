package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.draw.clip

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Catalog : Screen("catalog", "Inicio", Icons.Default.Home)
    object Splash : Screen("splash", "Splash", Icons.Default.Home)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
    object Cart : Screen("cart", "Carrito", Icons.Default.ShoppingCart)
    object Checkout : Screen("checkout", "Checkout", Icons.Default.Check)
    object Tracking : Screen("tracking", "Seguimiento", Icons.Default.LocalShipping)
    object AuthLogin : Screen("auth/login", "Login", Icons.Default.Person)
    object AuthRegister : Screen("auth/register", "Registro", Icons.Default.Person)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    // Crear una única instancia compartida de AuthViewModel ligada al Activity/Composición
    val authVm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AuthViewModel.Factory(context))
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, authVm = authVm)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            // Empezamos en Splash para verificar sesión y evitar parpadeos
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Splash: comprueba sesión y redirige una vez
            composable(Screen.Splash.route) {
                val loggedIn by authVm.sesionActiva.collectAsState(initial = false)
                // Espera corta para permitir que DataStore emita su valor
                var navigated by remember { mutableStateOf(false) }
                LaunchedEffect(loggedIn) {
                    if (!navigated) {
                        // Pequeña espera para sincronizar
                        kotlinx.coroutines.delay(200)
                        if (authVm.sesionActiva.value) {
                            navController.navigate(Screen.Catalog.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.AuthLogin.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                        navigated = true
                    }
                }
                // Mostrar pantalla temporal mientras decide
                PlaceholderScreen("Iniciando aplicación...")
            }

            composable(Screen.Catalog.route) { CatalogScreen(navController) }

            // Auth routes
            composable(Screen.AuthLogin.route) { LoginScreen(navController, authVm) }
            composable(Screen.AuthRegister.route) { RegisterScreen(navController) }

            // Product detail route
            composable("producto/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                ProductosScreen(navController = navController, productId = id)
            }

            // Profile: mostrar la pantalla y que la propia UI gestione la sesión (sin redirección automática)
            composable(Screen.Profile.route) {
                ProfileScreen(navController, authVm)
            }

            composable(Screen.Cart.route) { CartScreen(navController) }
            composable(Screen.Checkout.route) { CheckoutScreen() }
            composable(Screen.Tracking.route) { TrackingScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, authVm: AuthViewModel) {
    val items = listOf(
        Screen.Catalog,
        Screen.Profile,
        Screen.Cart,
        Screen.Checkout,
        Screen.Tracking
    )

    val context = LocalContext.current
    val avatar by authVm.avatar.collectAsState(initial = null)
    val loggedIn by authVm.sesionActiva.collectAsState(initial = false)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                icon = {
                    if (screen is Screen.Profile && loggedIn && avatar != null) {
                        // Mostrar avatar
                        // Hacemos una copia local para evitar problemas de smart-cast sobre la propiedad delegada
                        val avatarValue = avatar
                        val bmp = remember(avatarValue) {
                            avatarValue?.let { av ->
                                try {
                                    if (av.startsWith("/")) {
                                        BitmapFactory.decodeFile(av)
                                    } else {
                                        val uri = android.net.Uri.parse(av)
                                        val input = context.contentResolver.openInputStream(uri)
                                        input?.use { BitmapFactory.decodeStream(it) }
                                    }
                                } catch (e: Exception) {
                                    null
                                }
                            }
                        }
                        if (bmp != null) {
                            Image(bitmap = bmp.asImageBitmap(), contentDescription = "Avatar", modifier = Modifier.size(28.dp).clip(CircleShape))
                        } else {
                            Icon(screen.icon, contentDescription = screen.label)
                        }
                    } else {
                        Icon(screen.icon, contentDescription = screen.label)
                    }
                },
                label = { Text(screen.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
