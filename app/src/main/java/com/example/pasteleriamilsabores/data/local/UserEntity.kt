package com.example.pasteleriamilsabores.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val fechaNacimiento: String = "",
    val direccion: String = "",
    // Campos agregados para alinear con UserPreferences
    val telefono: String = "",
    val rut: String = "",
    val avatarUri: String? = null
)
