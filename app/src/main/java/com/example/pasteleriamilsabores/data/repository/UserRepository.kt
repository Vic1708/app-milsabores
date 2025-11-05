package com.example.pasteleriamilsabores.data.repository

import android.content.Context
import androidx.room.Room
import com.example.pasteleriamilsabores.data.local.AppDatabase
import com.example.pasteleriamilsabores.data.local.UserEntity

class UserRepository(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "pasteleria-db"
    ).build()

    private val userDao = db.userDao()

    /**
     * Intenta registrar un nuevo usuario.
     * Retorna el ID si fue exitoso, -1L si el email ya existe.
     */
    suspend fun register(user: UserEntity): Long {
        // Comprobar si email ya existe
        val existing = userDao.getByEmail(user.email)
        if (existing != null) return -1L
        return userDao.insert(user)
    }

    /**
     * Intenta hacer login con email y password.
     * Retorna el usuario si las credenciales son correctas, null si no.
     */
    suspend fun login(email: String, password: String): UserEntity? {
        val user = userDao.getByEmail(email) ?: return null
        return if (user.password == password) user else null
    }

    suspend fun getUserById(id: Int): UserEntity? = userDao.getById(id)

    suspend fun updateUser(user: UserEntity) = userDao.update(user)

    suspend fun clearAll() = userDao.clearAll()
}

