# 🍰 Pastelería Mil Sabores - App Android

![Estado](https://img.shields.io/badge/Estado-✅%20Completado-success)
![Plataforma](https://img.shields.io/badge/Plataforma-Android-green)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-✓-blue)
![Material3](https://img.shields.io/badge/Material%203-✓-purple)

> Aplicación Android con identidad visual coherente del proyecto React "Pastelería Mil Sabores"

---

## 📱 Descripción

Aplicación móvil para gestionar pedidos de una pastelería artesanal. Incluye catálogo de productos, carrito de compras, perfil de usuario con descuentos, checkout con validación y seguimiento de pedidos con animaciones.

**Características:**
- 🎨 Diseño coherente con versión web React
- 🧁 Paleta de colores cálida y acogedora
- ✨ Animaciones suaves y modernas
- 🏗️ Arquitectura MVVM limpia
- 📦 Material 3 con colores personalizados

---

## 🎯 Funcionalidades

### 🏠 Catálogo de Productos
- Lista de productos con nombre, descripción, imagen y precio
- Botón "Agregar al carrito" con animación de color
- Integración con ViewModel para gestión de estado
- Cards con diseño limpio y sombras suaves

### 👤 Perfil de Usuario
- Formulario completo: nombre, RUT, correo, fecha de nacimiento, dirección
- Cálculo automático de edad (java.time)
- Sistema de descuentos:
  - Correo @duocuc.cl → Torta gratis
  - Edad >= 50 años → 50% descuento
  - Código promocional FELICES50

### 🛒 Carrito de Compras
- Vista de productos agregados en tiempo real
- Eliminar productos individuales
- Vaciar carrito completo
- Cálculo automático del total
- Persistencia en memoria (StateFlow)

### 📦 Checkout / Envío
- Formulario de envío completo
- Validación de todos los campos
- Validación de formato de fecha (AAAA-MM-DD)
- Mensaje de confirmación animado
- Limpieza automática del carrito

### 🚚 Seguimiento de Pedido
- 4 estados visuales: Pendiente → Preparación → Camino → Entregado
- Barra de progreso animada
- Transición automática cada 2.5 segundos
- Cambio de color dinámico según estado
- Porcentaje de completado visible

---

## 🎨 Identidad Visual

### Paleta de Colores
```
🎨 CremaClaro    #FFF8E7  Fondo principal
🌿 VerdePastel   #A5C882  TopAppBar, acentos
🍊 NaranjaSuave  #F4A261  Botones principales
🍫 Chocolate     #4E342E  Texto principal
🟤 MarronSuave   #8C6A47  Texto secundario
⚪ BlancoPuro    #FFFFFF  Cards, contraste
🌫️ GrisSuave     #EEECE8  Fondos secundarios
🌲 VerdeOscuro   #5B8C5A  Énfasis fuerte
🔥 NaranjaOscuro #E76F51  Alertas, acciones
```

### Estilos Aplicados
- **TopAppBar:** VerdePastel con bordes redondeados 16dp
- **Botones:** NaranjaSuave con shape 12dp
- **Cards:** BlancoPuro con shape 16dp y sombras suaves
- **Textos:** Chocolate (títulos), MarronSuave (descripciones)
- **Fondos:** CremaClaro para sensación cálida

---

## ✨ Animaciones

| Tipo | Ubicación | Descripción |
|------|-----------|-------------|
| **scaleIn** | Catálogo | Cards aparecen con spring bounce |
| **animateColorAsState** | Botones | NaranjaSuave → VerdeOscuro al presionar |
| **slideIn + fadeIn** | Checkout | Mensaje de validación suave |
| **animateFloatAsState** | Tracking | Barra de progreso animada |

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────┐
│           View (Compose)            │
│  CatalogScreen, CartScreen, etc.    │
└──────────────┬──────────────────────┘
               │
               ↓
┌─────────────────────────────────────┐
│      ViewModel (StateFlow)          │
│         CartViewModel               │
└──────────────┬──────────────────────┘
               │
               ↓
┌─────────────────────────────────────┐
│         Model (Data Class)          │
│            Producto                 │
└─────────────────────────────────────┘
```

**Patrón:** MVVM (Model-View-ViewModel)
- **Model:** `Producto.kt`
- **ViewModel:** `CartViewModel.kt` (gestión de estado con StateFlow)
- **View:** Screens con Jetpack Compose

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/pasteleriamilsabores/
├── MainActivity.kt              # Activity principal
├── model/
│   └── Producto.kt             # Data class para productos
├── viewmodel/
│   └── CartViewModel.kt        # Lógica del carrito
└── ui/
    ├── theme/
    │   ├── Color.kt            # Paleta de colores
    │   ├── Type.kt             # Tipografía
    │   └── theme.kt            # Theme configuration
    └── screens/
        ├── Navigation.kt       # Navegación con Compose
        ├── CatalogScreen.kt    # Pantalla de catálogo
        ├── CartScreen.kt       # Pantalla de carrito
        ├── ProfileScreen.kt    # Pantalla de perfil
        ├── CheckoutScreen.kt   # Pantalla de checkout
        └── TrackingScreen.kt   # Pantalla de seguimiento
```

---

## 🚀 Inicio Rápido

### Requisitos
- Android Studio Arctic Fox o superior
- Kotlin 1.9+
- Gradle 8.0+
- Android SDK 24+ (Android 7.0)

### Compilar y Ejecutar

1. **Clonar/Abrir proyecto:**
   ```bash
   # Ya tienes el proyecto en:
   C:\Users\Usuario\AndroidStudioProjects\PasteleriaMilSabores
   ```

2. **Sincronizar Gradle:**
   ```
   File → Sync Project with Gradle Files
   ```

3. **Compilar:**
   ```
   Build → Make Project
   ```

4. **Ejecutar:**
   ```
   Run → Run 'app' (Shift+F10)
   ```

**Ver guía completa:** [GUIA_COMPILACION.md](GUIA_COMPILACION.md)

---

## 📚 Documentación

- 📖 [**GUIA_COMPILACION.md**](GUIA_COMPILACION.md) - Instrucciones paso a paso
- 🎨 [**DISEÑO_VISUAL.md**](DISEÑO_VISUAL.md) - Identidad visual completa
- ✅ [**IMPLEMENTACION_COMPLETA.md**](IMPLEMENTACION_COMPLETA.md) - Resumen detallado

---

## 🛠️ Tecnologías

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose
- **Material:** Material 3
- **Arquitectura:** MVVM
- **Navegación:** Navigation Compose
- **Estado:** StateFlow (Kotlin Coroutines)
- **Recursos nativos:** java.time (LocalDate, Period)

---

## 📊 Estado del Proyecto

- ✅ Todas las pantallas implementadas
- ✅ Identidad visual aplicada
- ✅ Animaciones funcionando
- ✅ MVVM implementado
- ✅ Navegación funcional
- ✅ Validaciones completas
- ✅ Sin errores de compilación
- ✅ Listo para ejecutar

---

## 🎯 Evaluación

### Funcionalidades (E2 + E3)

| Requisito | Estado |
|-----------|--------|
| Catálogo con productos | ✅ |
| Perfil con formulario completo | ✅ |
| Carrito funcional | ✅ |
| Checkout con validación | ✅ |
| Seguimiento animado | ✅ |
| Diseño coherente | ✅ |
| MVVM | ✅ |
| Persistencia simulada | ✅ |
| Recursos nativos | ✅ |
| Animaciones | ✅ |
| Navegación | ✅ |

---

## 📸 Capturas (Simuladas)

```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   Catálogo   │  │   Carrito    │  │    Perfil    │
│   🍰 🧁 🍰   │  │   3 items    │  │  Formulario  │
│   Products   │  │   Total: $   │  │   Completo   │
└──────────────┘  └──────────────┘  └──────────────┘

┌──────────────┐  ┌──────────────┐
│   Checkout   │  │  Seguimiento │
│  Validación  │  │  ▓▓▓▓▓░░░░░  │
│    Activa    │  │   50% ✓     │
└──────────────┘  └──────────────┘
```

---

## 🤝 Contribución

Proyecto académico - **Pastelería Mil Sabores**

**Desarrollado por:** [Tu Nombre]  
**Institución:** DuocUC  
**Fecha:** Octubre 2025  
**Evaluación:** E2 + E3  

---

## 📝 Licencia

Proyecto educativo - Uso académico

---

## 🎓 Defensa Individual

### Preparación:

**Explicar:**
- Arquitectura MVVM
- StateFlow y gestión de estado
- Animaciones con Compose
- Identidad visual coherente

**Demostrar:**
- Flujo completo: Catálogo → Carrito → Checkout → Tracking
- Cálculo de edad y descuentos
- Validaciones en tiempo real
- Animaciones suaves

**Modificar en vivo:**
- Cambiar color de un botón
- Añadir producto al catálogo
- Ajustar lógica de descuentos

---

## 🌟 Características Destacadas

✨ **Coherencia visual total** con proyecto React  
✨ **Animaciones profesionales** y suaves  
✨ **Código limpio** y mantenible  
✨ **Arquitectura escalable** (MVVM)  
✨ **Material 3** con personalización completa  
✨ **Experiencia de usuario** cálida y acogedora  

---

## 📞 Soporte

Si tienes problemas:
1. Ver [GUIA_COMPILACION.md](GUIA_COMPILACION.md)
2. Revisar Logcat en Android Studio
3. Limpiar proyecto: `Build → Clean Project`
4. Invalidar caché: `File → Invalidate Caches`

---

**Hecho con 🍰 para Pastelería Mil Sabores**

---

## ⚡ Quick Start

```bash
# 1. Abrir Android Studio
# 2. Abrir proyecto en: C:\Users\Usuario\AndroidStudioProjects\PasteleriaMilSabores
# 3. Sync Gradle
# 4. Run app
```

**¡Listo para compilar y ejecutar! ✅**

