package com.example.pasteleriamilsabores.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.local.CartItemEntity
import com.example.pasteleriamilsabores.data.local.OrderEntity
import com.example.pasteleriamilsabores.data.repository.Repository
import com.example.pasteleriamilsabores.model.Producto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.pasteleriamilsabores.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 🛒 CartViewModel
 * Controla el carrito de compras con datos persistentes en Room.
 * Mantiene compatibilidad con el modelo "Producto" del catálogo.
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {

    // 🧩 Conexión al repositorio (Singleton)
    private val repository = Repository.getInstance(application)

    // 🔹 Items del carrito almacenados en base de datos (Flow)
    val cartItems: StateFlow<List<CartItemEntity>> =
        repository.obtenerCartItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // -------------------------------
    // 🧁 Funciones principales
    // -------------------------------

    /**
     * Agrega un producto al carrito y lo guarda en Room.
     * Si el producto ya existe en el carrito, incrementa la cantidad.
     */
    fun addToCart(producto: Producto, quantity: Int = 1) {
        viewModelScope.launch {
            // buscar si ya existe el item en el carrito
            val existing = cartItems.value.find { it.productId == producto.id }
            if (existing != null) {
                val updated = existing.copy(quantity = existing.quantity + quantity)
                repository.actualizarCartItem(updated)
            } else {
                val entity = CartItemEntity(
                    id = 0,
                    productId = producto.id,
                    nombre = producto.nombre,
                    descripcion = producto.descripcion,
                    precio = producto.precio.toDouble(),
                    imagen = producto.imagen,
                    quantity = quantity
                )
                repository.agregarCartItem(entity)
            }
        }
    }

    /**
     * Elimina un producto del carrito por productId.
     */
    fun removeFromCart(producto: Producto) {
        viewModelScope.launch {
            val current = repository.obtenerCartItems()
                .map { it.find { item -> item.productId == producto.id } }
                .stateIn(viewModelScope)
                .value

            current?.let {
                repository.eliminarCartItem(it)
            }
        }
    }

    /**
     * Limpia el carrito completo.
     */
    fun clearCart() {
        viewModelScope.launch {
            repository.limpiarCarrito()
        }
    }

    /**
     * Calcula el total de la compra.
     */
    fun calcularTotal(): Double {
        return cartItems.value.sumOf { it.precio * it.quantity }
    }

    /**
     * 📦 Crea un pedido con los datos del checkout
     * Genera un número único para el pedido y retorna la orden creada
     */
    suspend fun createOrder(
        direccion: String,
        comuna: String,
        fechaEntrega: String,
        telefono: String
    ): OrderEntity? {
        return try {
            val total = calcularTotal()

            // Generar número único: PED-YYYYMMDD-XXXXX
            val timestamp = SimpleDateFormat("yyyyMMdd", Locale("es", "CL")).format(Date())
            val random = (10000..99999).random()
            val orderNumber = "PED-$timestamp-$random"

            val order = OrderEntity(
                orderNumber = orderNumber,
                estado = "Pendiente 📝",
                total = total,
                direccion = direccion,
                comuna = comuna,
                fechaEntrega = fechaEntrega,
                telefono = telefono,
                progreso = 0  // 0 = Pendiente
            )

            // Guardar la orden en BD
            repository.crearOrden(order)

            return order
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 📦 Obtiene la orden más reciente (pedido actual)
     */
    suspend fun obtenerOrdenReciente() = repository.obtenerOrdenReciente()

    /**
     * 🔄 Actualiza el estado de una orden
     */
    suspend fun actualizarOrden(order: OrderEntity) {
        repository.actualizarOrden(order)
    }

    // -------------------------------
    // 🧠 Compatibilidad con el modelo UI
    // -------------------------------

    /**
     * Devuelve el carrito como lista del modelo Producto (UI-friendly).
     */
    val cartAsProducto: StateFlow<List<Producto>> =
        cartItems.map { list ->
            list.map {
                val imgRes = if (it.imagen != 0) it.imagen else R.drawable.brownie
                Producto(
                    id = it.productId,
                    nombre = it.nombre,
                    descripcion = it.descripcion,
                    precio = it.precio.toInt(),
                    imagen = imgRes,
                    categoria = ""
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // -------------------------------
    // 🔧 Factory para integrarlo desde Composables
    // -------------------------------
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
