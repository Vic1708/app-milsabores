package com.example.pasteleriamilsabores.data

import com.example.pasteleriamilsabores.model.Producto

object DefaultProducts {
    val productos: List<Producto> = listOf(
        // Tortas Cuadradas
        Producto(101, "TC-001", "Torta Cuadrada Vainilla", "Torta cuadrada clásica de vainilla con betún artesanal.", 14990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Cuadradas"),
        Producto(102, "TC-002", "Torta Cuadrada Chocolate", "Base de chocolate, relleno de crema y cobertura de ganache.", 16990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Cuadradas"),
        Producto(103, "TC-003", "Torta Cuadrada Red Velvet", "Suave red velvet con queso crema y decoración artesanal.", 17990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Cuadradas"),

        // Tortas Circulares
        Producto(201, "TR-001", "Torta Circular Chocolate", "Torta circular de chocolate con capas húmedas y ganache.", 15990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Circulares"),
        Producto(202, "TR-002", "Torta Circular Frutas", "Torta circular con crema y frutas frescas de temporada.", 16990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Circulares"),
        Producto(203, "TR-003", "Torta Circular Dulce de Leche", "Con relleno de dulce de leche y cobertura crocante.", 17990, com.example.pasteleriamilsabores.R.drawable.brownie, "Tortas Circulares"),

        // Postres Individuales
        Producto(301, "PI-001", "Brownie Individual", "Brownie artesanal con chispas de chocolate.", 2490, com.example.pasteleriamilsabores.R.drawable.brownie, "Postres Individuales"),
        Producto(302, "PI-002", "Cupcake Vainilla", "Cupcake esponjoso con frosting de vainilla.", 1990, com.example.pasteleriamilsabores.R.drawable.brownie, "Postres Individuales"),
        Producto(303, "PI-003", "Cheesecake Mini", "Mini cheesecake con base de galleta y topping de frutilla.", 2990, com.example.pasteleriamilsabores.R.drawable.brownie, "Postres Individuales"),

        // Vegana
        Producto(401, "VG-001", "Torta Vegana Chocolate", "Torta vegana húmeda de chocolate con crema vegana.", 18990, com.example.pasteleriamilsabores.R.drawable.brownie, "Vegana"),
        Producto(402, "VG-002", "Cupcake Vegano", "Cupcake vegano con glaseado natural.", 2190, com.example.pasteleriamilsabores.R.drawable.brownie, "Vegana"),
        Producto(403, "VG-003", "Brownie Vegano", "Brownie vegano con nueces.", 2390, com.example.pasteleriamilsabores.R.drawable.brownie, "Vegana"),

        // Sin Gluten
        Producto(501, "SG-001", "Torta Sin Gluten Vainilla", "Torta sin gluten, ideal para dietas especiales.", 17990, com.example.pasteleriamilsabores.R.drawable.brownie, "Sin Gluten"),
        Producto(502, "SG-002", "Muffin Sin Gluten", "Muffin esponjoso sin gluten con chips de chocolate.", 2190, com.example.pasteleriamilsabores.R.drawable.brownie, "Sin Gluten"),
        Producto(503, "SG-003", "Galletas Sin Gluten", "Paquete de galletas crujientes sin gluten.", 1290, com.example.pasteleriamilsabores.R.drawable.brownie, "Sin Gluten")
    )
}

