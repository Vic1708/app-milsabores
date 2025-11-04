package com.example.pasteleriamilsabores.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM productos")
    fun obtenerProductos(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerProductoPorId(id: Int): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarProducto(producto: ProductEntity)

    @Update
    suspend fun actualizarProducto(producto: ProductEntity)

    @Delete
    suspend fun eliminarProducto(producto: ProductEntity)
}
