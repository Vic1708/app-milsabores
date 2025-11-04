package com.example.pasteleriamilsabores.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): UserEntity?

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun eliminarUsuario(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearAll()

    // Método adicional para listar todos los usuarios (Flow)
    @Query("SELECT * FROM users")
    fun obtenerUsuarios(): Flow<List<UserEntity>>
}
