package com.example.pasteleriamilsabores.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 📦 Entidad de Pedido
 * Almacena información del pedido incluyendo número, estado y progreso
 */
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderNumber: String,              // Ej: "PED-20250104-001"
    val estado: String,                    // Pendiente, En preparación, En reparto, Entregado
    val total: Double,
    val direccion: String,
    val comuna: String,
    val fechaEntrega: String,
    val telefono: String,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val progreso: Int = 0                  // 0=Pendiente, 1=Preparación, 2=Reparto, 3=Entregado
)

