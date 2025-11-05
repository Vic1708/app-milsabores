package com.example.pasteleriamilsabores.data.repository

import com.example.pasteleriamilsabores.data.local.CartItemEntity
import com.example.pasteleriamilsabores.data.local.OrderEntity
import com.example.pasteleriamilsabores.data.local.ProductEntity
import com.example.pasteleriamilsabores.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz pública del repositorio.
 * Define las operaciones disponibles para usuarios, productos y carrito.
 */
interface IRepository {

    // Usuarios
    fun obtenerUsuarios(): Flow<List<UserEntity>>
    suspend fun obtenerUsuarioPorId(id: Int): UserEntity?
    suspend fun obtenerUsuarioPorEmail(email: String): UserEntity?
    suspend fun agregarUsuario(usuario: UserEntity): Long
    suspend fun actualizarUsuario(usuario: UserEntity)
    suspend fun eliminarUsuario(usuario: UserEntity)
    suspend fun limpiarUsuarios()

    // Productos
    fun obtenerProductos(): Flow<List<ProductEntity>>
    suspend fun obtenerProductoPorId(id: Int): ProductEntity?
    suspend fun agregarProducto(producto: ProductEntity)
    suspend fun actualizarProducto(producto: ProductEntity)
    suspend fun eliminarProducto(producto: ProductEntity)

    // Carrito
    fun obtenerCartItems(): Flow<List<CartItemEntity>>
    suspend fun agregarCartItem(item: CartItemEntity)
    suspend fun actualizarCartItem(item: CartItemEntity)
    suspend fun eliminarCartItem(item: CartItemEntity)
    suspend fun limpiarCarrito()

    // Órdenes
    suspend fun crearOrden(orden: OrderEntity): Long
    suspend fun obtenerOrdenPorNumero(orderNumber: String): OrderEntity?
    suspend fun obtenerOrdenReciente(): OrderEntity?
    fun obtenerOrdenRecienteFlow(): Flow<OrderEntity?>
    suspend fun actualizarOrden(orden: OrderEntity)
    fun obtenerTodasLasOrdenes(): Flow<List<OrderEntity>>
}

