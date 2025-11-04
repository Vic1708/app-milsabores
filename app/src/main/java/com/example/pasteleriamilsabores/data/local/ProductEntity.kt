package com.example.pasteleriamilsabores.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean,
    val imagen: Int, // ahora guardamos el resource id directamente como Int
    val categoria: String = "Sin categoría"
)
