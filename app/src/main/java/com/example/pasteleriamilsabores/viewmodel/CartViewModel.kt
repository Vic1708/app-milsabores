package com.example.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasteleriamilsabores.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<Producto>>(emptyList())
    val cartItems: StateFlow<List<Producto>> = _cartItems

    fun addToCart(producto: Producto) {
        _cartItems.update { it + producto }
    }

    fun removeFromCart(producto: Producto) {
        _cartItems.update { current ->
            current.filterNot { it.id == producto.id }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun calcularTotal(): Int {
        return _cartItems.value.sumOf { it.precio }
    }
}
