package com.example.pasteleriamilsabores.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors
import com.example.pasteleriamilsabores.data.DefaultProducts
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

// Añadimos todas las entidades conocidas para que Room genere los DAOs correspondientes
@Database(entities = [UserEntity::class, ProductEntity::class, CartItemEntity::class, OrderEntity::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        private const val PREPOPULATE = true // Prepopular la BD con los 15 productos del repositorio

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pasteleria-db"
                )
                    // En desarrollo: si el esquema cambia, destruir y recrear la base para evitar errores de validación
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                if (PREPOPULATE) {
                    // Pre-popular la tabla de productos con los datos estáticos del repositorio
                    Executors.newSingleThreadExecutor().execute {
                        try {
                            runBlocking {
                                val existing = instance.productDao().obtenerProductos().first()
                                if (existing.isEmpty()) {
                                    val repoList = DefaultProducts.productos
                                    repoList.forEach { p ->
                                        val entity = ProductEntity(
                                            id = p.id, // mantener el id del repositorio para navegación por id
                                            nombre = p.nombre,
                                            descripcion = p.descripcion,
                                            precio = p.precio.toDouble(),
                                            disponible = true,
                                            imagen = p.imagen,
                                            categoria = p.categoria
                                        )
                                        instance.productDao().agregarProducto(entity)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                instance
            }
        }
    }
}
