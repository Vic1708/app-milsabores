package com.example.pasteleriamilsabores.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: Int,
    val quantity: Int = 1
)

