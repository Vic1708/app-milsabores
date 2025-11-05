package com.example.pasteleriamilsabores.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    /**
     * Inserta un nuevo pedido en la base de datos
     */
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    /**
     * Obtiene un pedido por su número
     */
    @Query("SELECT * FROM orders WHERE orderNumber = :orderNumber LIMIT 1")
    suspend fun getOrderByNumber(orderNumber: String): OrderEntity?

    /**
     * Obtiene el pedido más reciente (el actual)
     */
    @Query("SELECT * FROM orders ORDER BY fechaCreacion DESC LIMIT 1")
    suspend fun getLatestOrder(): OrderEntity?

    /**
     * Obtiene el pedido más reciente como Flow (para observar cambios)
     */
    @Query("SELECT * FROM orders ORDER BY fechaCreacion DESC LIMIT 1")
    fun getLatestOrderFlow(): Flow<OrderEntity?>

    /**
     * Obtiene todos los pedidos del usuario
     */
    @Query("SELECT * FROM orders ORDER BY fechaCreacion DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    /**
     * Actualiza un pedido existente
     */
    @Update
    suspend fun updateOrder(order: OrderEntity)

    /**
     * Elimina un pedido
     */
    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    /**
     * Limpia todos los pedidos
     */
    @Query("DELETE FROM orders")
    suspend fun clearAll()
}

