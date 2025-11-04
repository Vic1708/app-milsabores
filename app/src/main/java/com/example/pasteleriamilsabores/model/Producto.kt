package com.example.pasteleriamilsabores.model

data class Producto(
    val id: Int,
    val code: String = "",
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val imagen: Int,
    val categoria: String = "Sin categoría", // valor por defecto para compatibilidad
    val disponible: Boolean = true // agregado para sincronizar con la entidad Room
)
