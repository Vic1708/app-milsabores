package com.example.pasteleriamilsabores.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items")
    fun obtenerCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarCartItem(item: CartItemEntity)

    @Update
    suspend fun actualizarCartItem(item: CartItemEntity)

    @Delete
    suspend fun eliminarCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun limpiarCarrito()
}

