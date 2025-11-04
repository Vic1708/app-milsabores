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

    suspend fun register(user: UserEntity): Long {
        // comprobar si email ya existe
        val existing = userDao.getByEmail(user.email)
        if (existing != null) return -1
        return userDao.insert(user)
    }

    suspend fun login(email: String, password: String): UserEntity? {
        val user = userDao.getByEmail(email) ?: return null
        return if (user.password == password) user else null
    }

    suspend fun getUserById(id: Int): UserEntity? = userDao.getById(id)

    suspend fun updateUser(user: UserEntity) = userDao.update(user)

    suspend fun clearAll() = userDao.clearAll()
}

