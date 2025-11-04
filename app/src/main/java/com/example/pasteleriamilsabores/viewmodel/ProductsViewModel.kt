package com.example.pasteleriamilsabores.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.ProductsRepository
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.model.Producto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ProductsViewModel
 * Centraliza el acceso a la lista de productos (persistencia en Room a través de Repository).
 * Ahora contiene una lista por defecto de 15 productos (3 por categoría) que se usa cuando
 * la base de datos está vacía para facilitar pruebas y subida inicial.
 */
class ProductsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository.getInstance(application)

    // Lista por defecto (15 productos: 3 por categoría)
    private val defaultProducts: List<Producto> = listOf(
        // Tortas Cuadradas
        Producto(101, "TC-001", "Torta Cuadrada Vainilla", "Torta cuadrada clásica de vainilla con betún artesanal.", 14990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Cuadradas"),
        Producto(102, "TC-002", "Torta Cuadrada Chocolate", "Base de chocolate, relleno de crema y cobertura de ganache.", 16990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Cuadradas"),
        Producto(103, "TC-003", "Torta Cuadrada Red Velvet", "Suave red velvet con queso crema y decoración artesanal.", 17990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Cuadradas"),

        // Tortas Circulares
        Producto(201, "TR-001", "Torta Circular Chocolate", "Torta circular de chocolate con capas húmedas y ganache.", 15990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Circulares"),
        Producto(202, "TR-002", "Torta Circular Frutas", "Torta circular con crema y frutas frescas de temporada.", 16990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Circulares"),
        Producto(203, "TR-003", "Torta Circular Dulce de Leche", "Con relleno de dulce de leche y cobertura crocante.", 17990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Circulares"),

        // Postres Individuales
        Producto(301, "PI-001", "Brownie Individual", "Brownie artesanal con chispas de chocolate.", 2490, com.example.pasteleriamilsabores.R.drawable.brownie, "Postres Individuales"),
        Producto(302, "PI-002", "Cupcake Vainilla", "Cupcake esponjoso con frosting de vainilla.", 1990, com.example.pasteleriamilsabores.R.drawable.brownie, "Postres Individuales"),
        Producto(303, "PI-003", "Cheesecake Mini", "Mini cheesecake con base de galleta y topping de frutilla.", 2990, com.example.pasteleriamilsabores.R.drawable.brownie, "Postres Individuales"),

        // Vegana
        Producto(401, "VG-001", "Torta Vegana Chocolate", "Torta vegana húmeda de chocolate con crema vegana.", 18990, com.example.pasteleriamilsabores.R.drawable.brownie, "Vegana"),
        Producto(402, "VG-002", "Cupcake Vegano", "Cupcake vegano con glaseado natural.", 2190, com.example.pasteleriamilsabores.R.drawable.brownie, "Vegana"),
        Producto(403, "VG-003", "Brownie Vegano", "Brownie vegano con nueces.", 2390, com.example.pasteleriamilsabores.R.drawable.brownie, "Vegana"),

        // Sin Gluten
        Producto(501, "SG-001", "Torta Sin Gluten Vainilla", "Torta sin gluten, ideal para dietas especiales.", 17990, com.example.pasteleriamilsabores.R.drawable.brownie, "Sin Gluten"),
        Producto(502, "SG-002", "Muffin Sin Gluten", "Muffin esponjoso sin gluten con chips de chocolate.", 2190, com.example.pasteleriamilsabores.R.drawable.brownie, "Sin Gluten"),
        Producto(503, "SG-003", "Galletas Sin Gluten", "Paquete de galletas crujientes sin gluten.", 1290, com.example.pasteleriamilsabores.R.drawable.brownie, "Sin Gluten")
    )

    // Exponer los productos como StateFlow de modelo UI `Producto`.
    // Si la BD devuelve lista vacía, mostramos la lista por defecto local.
    val productos: StateFlow<List<Producto>> = repository.obtenerProductos()
        .map { list ->
            if (list.isEmpty()) {
                defaultProducts
            } else {
                list.map { pe ->
                    Producto(
                        id = pe.id,
                        code = "",
                        nombre = pe.nombre,
                        descripcion = pe.descripcion,
                        precio = pe.precio.toInt(),
                        imagen = pe.imagen,
                        categoria = pe.categoria ?: "Sin categoría"
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = defaultProducts
        )

    // Obtener categorías desde el flujo actual
    fun getCategories(): List<String> {
        // Construir la lista a partir de los productos actuales manteniendo orden y agregando "Todas las categorías"
        val cats = productos.value.map { it.categoria }.distinct().toMutableList()
        cats.sort()
        cats.add(0, "Todas las categorías")
        return cats
    }

    fun getProductById(id: Int): Producto? = productos.value.find { it.id == id }

    fun filterProducts(category: String, searchQuery: String): List<Producto> {
        val q = searchQuery.trim().lowercase()
        return productos.value.filter { p ->
            val matchesCategory = (category == "Todas las categorías") || p.categoria == category
            val matchesQuery = q.isEmpty() || p.nombre.lowercase().contains(q) || p.code.lowercase().contains(q)
            matchesCategory && matchesQuery
        }
    }

    // Añadir producto (persistente)
    fun addProduct(producto: Producto) {
        viewModelScope.launch {
            repository.agregarProducto(
                com.example.pasteleriamilsabores.data.local.ProductEntity(
                    id = producto.id,
                    nombre = producto.nombre,
                    descripcion = producto.descripcion,
                    precio = producto.precio.toDouble(),
                    disponible = true,
                    imagen = producto.imagen,
                    categoria = producto.categoria
                )
            )
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
