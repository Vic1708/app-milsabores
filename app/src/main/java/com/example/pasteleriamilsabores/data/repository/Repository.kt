package com.example.pasteleriamilsabores.data.repository

import android.content.Context
import com.example.pasteleriamilsabores.data.local.AppDatabase
import com.example.pasteleriamilsabores.data.local.CartItemEntity
import com.example.pasteleriamilsabores.data.local.ProductEntity
import com.example.pasteleriamilsabores.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * 🍰 Repository
 * Repositorio central para la app Pastelería Mil Sabores.
 * Provee acceso unificado a la base de datos local (Room)
 * mediante DAOs de usuarios y productos.
 */
class Repository(private val db: AppDatabase) : IRepository {

    // -------------------------------
    // 🧁 SECCIÓN DE USUARIOS
    // -------------------------------

    override fun obtenerUsuarios(): Flow<List<UserEntity>> =
        db.userDao().obtenerUsuarios()

    override suspend fun obtenerUsuarioPorId(id: Int): UserEntity? =
        db.userDao().getById(id)

    override suspend fun obtenerUsuarioPorEmail(email: String): UserEntity? =
        db.userDao().getByEmail(email)

    override suspend fun agregarUsuario(usuario: UserEntity): Long =
        db.userDao().insert(usuario)

    override suspend fun actualizarUsuario(usuario: UserEntity) =
        db.userDao().update(usuario)

    override suspend fun eliminarUsuario(usuario: UserEntity) =
        db.userDao().eliminarUsuario(usuario)

    override suspend fun limpiarUsuarios() = db.userDao().clearAll()

    // -------------------------------
    // 🍪 SECCIÓN DE PRODUCTOS
    // -------------------------------

    override fun obtenerProductos(): Flow<List<ProductEntity>> =
        db.productDao().obtenerProductos()

    override suspend fun obtenerProductoPorId(id: Int): ProductEntity? =
        db.productDao().obtenerProductoPorId(id)

    override suspend fun agregarProducto(producto: ProductEntity) =
        db.productDao().agregarProducto(producto)

    override suspend fun actualizarProducto(producto: ProductEntity) =
        db.productDao().actualizarProducto(producto)

    override suspend fun eliminarProducto(producto: ProductEntity) =
        db.productDao().eliminarProducto(producto)

    // -------------------------------
    // 🛒 SECCIÓN DE CARRITO
    // -------------------------------

    override fun obtenerCartItems(): Flow<List<CartItemEntity>> =
        db.cartDao().obtenerCartItems()

    override suspend fun agregarCartItem(item: CartItemEntity) =
        db.cartDao().agregarCartItem(item)

    override suspend fun actualizarCartItem(item: CartItemEntity) =
        db.cartDao().actualizarCartItem(item)

    override suspend fun eliminarCartItem(item: CartItemEntity) =
        db.cartDao().eliminarCartItem(item)

    override suspend fun limpiarCarrito() = db.cartDao().limpiarCarrito()

    // -------------------------------
    // ☕ Singleton para obtener una instancia global
    // -------------------------------

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(context: Context): Repository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getInstance(context)
                val instance = Repository(database)
                INSTANCE = instance
                instance
            }
        }
    }
}
