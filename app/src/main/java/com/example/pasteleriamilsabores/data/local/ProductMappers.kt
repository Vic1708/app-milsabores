package com.example.pasteleriamilsabores.data.local

import com.example.pasteleriamilsabores.model.Producto

// Mapeadores entre la entidad Room y el modelo de UI
fun ProductEntity.toProducto(): Producto = Producto(
    id = this.id,
    code = "", // si necesitas el code, agrégalo a la entidad
    nombre = this.nombre,
    descripcion = this.descripcion,
    precio = this.precio.toInt(),
    imagen = this.imagen,
    categoria = "Sin categoría",
    disponible = this.disponible
)

fun Producto.toEntity(): ProductEntity = ProductEntity(
    id = this.id,
    nombre = this.nombre,
    descripcion = this.descripcion,
    precio = this.precio.toDouble(),
    disponible = this.disponible,
    imagen = this.imagen
)

