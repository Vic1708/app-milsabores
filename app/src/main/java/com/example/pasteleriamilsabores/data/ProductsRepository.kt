package com.example.pasteleriamilsabores.data

import com.example.pasteleriamilsabores.model.Producto

object ProductsRepository {
    // Delegar a DefaultProducts para mantener un único origen de verdad
    fun getCategories(): List<String> {
        val cats = DefaultProducts.productos.map { it.categoria }.distinct().toMutableList()
        cats.sort()
        cats.add(0, "Todas las categorías")
        return cats
    }

    fun filterProducts(category: String, searchQuery: String): List<Producto> {
        val q = searchQuery.trim().lowercase()
        return DefaultProducts.productos.filter { p ->
            val matchesCategory = (category == "Todas las categorías") || p.categoria == category
            val matchesQuery = q.isEmpty() || p.nombre.lowercase().contains(q) || p.code.lowercase().contains(q)
            matchesCategory && matchesQuery
        }
    }

    fun getProductById(id: Int): Producto? = DefaultProducts.productos.find { it.id == id }
}
