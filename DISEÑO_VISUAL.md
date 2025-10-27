# 🎨 Diseño Visual — Pastelería Mil Sabores

## Identidad Visual Implementada

Este proyecto mantiene **coherencia visual completa** con el proyecto React web "Pastelería Mil Sabores", adaptado profesionalmente a **Android con Jetpack Compose / Material 3**.

---

## 🎨 Paleta de Colores

```kotlin
CremaClaro   = #FFF8E7  // Fondo principal cálido
VerdePastel  = #A5C882  // TopAppBar, acentos suaves
NaranjaSuave = #F4A261  // Botones principales, énfasis
MarronSuave  = #8C6A47  // Texto secundario, bordes
BlancoPuro   = #FFFFFF  // Cards, contraste limpio
GrisSuave    = #EEECE8  // Fondos secundarios, tarjetas
VerdeOscuro  = #5B8C5A  // Énfasis fuerte, completado
NaranjaOscuro= #E76F51  // Acciones destacadas, alertas
Chocolate    = #4E342E  // Texto principal, headers
```

**Aplicación:**
- Fondos: `CremaClaro`
- TopAppBar: `VerdePastel` con bordes redondeados 16dp
- Botones: `NaranjaSuave` con shape 12dp
- Cards: `BlancoPuro` con shape 16dp
- Textos: `Chocolate` (títulos), `MarronSuave` (descripciones)

---

## ✍️ Tipografía

**Actual:** SansSerif del sistema (similar a Poppins)

**Para mejorar (opcional):**

### 📥 Cómo añadir fuente Poppins

1. **Descargar Poppins desde Google Fonts:**
   - Ir a [Google Fonts - Poppins](https://fonts.google.com/specimen/Poppins)
   - Seleccionar pesos: Regular (400), Medium (500), Bold (700)
   - Descargar archivos `.ttf`

2. **Crear carpeta de fuentes:**
   ```
   app/src/main/res/font/
   ```

3. **Copiar archivos:**
   ```
   poppins_regular.ttf
   poppins_medium.ttf
   poppins_bold.ttf
   ```

4. **Actualizar `Type.kt`:**
   ```kotlin
   val Poppins = FontFamily(
       Font(R.font.poppins_regular, FontWeight.Normal),
       Font(R.font.poppins_medium, FontWeight.Medium),
       Font(R.font.poppins_bold, FontWeight.Bold)
   )
   
   // Luego cambiar fontFamily = FontFamily.SansSerif
   // por fontFamily = Poppins
   ```

---

## 📱 Estilos por Pantalla

### 🏠 CatalogScreen
- **TopAppBar:** VerdePastel, bordes redondeados 16dp, sombra 4dp
- **Cards:** BlancoPuro, shape 16dp, padding 8dp, elevation 6dp
- **Botones:** NaranjaSuave → VerdeOscuro (animación al presionar)
- **Animación:** scaleIn con spring bounce
- **Textos:** 
  - Nombre: Chocolate, Bold, 20sp
  - Descripción: MarronSuave, Medium, 14sp
  - Precio: Chocolate, Bold, 18sp

### 🛒 CartScreen
- **TopAppBar:** VerdePastel con bordes redondeados
- **Items:** Cards BlancoPuro con shape 12dp
- **Total:** Card GrisSuave, texto Chocolate Bold
- **Botones:** 
  - Vaciar: OutlinedButton, color NaranjaOscuro
  - Checkout: Button NaranjaSuave

### 👤 ProfileScreen
- **TopAppBar:** VerdePastel
- **Campos:** OutlinedTextField con:
  - Shape: 12dp
  - Border focused: VerdePastel
  - Border unfocused: GrisSuave
- **Botón calcular:** NaranjaSuave, shape 12dp
- **Resultados:** Card GrisSuave con textos Chocolate/MarronSuave

### 📦 CheckoutScreen
- **Formulario:** Mismos estilos que ProfileScreen
- **Validación:** AnimatedVisibility con slideIn + fadeIn
- **Mensajes:**
  - Éxito: GrisSuave background, VerdeOscuro texto
  - Error: NaranjaOscuro background alpha 0.1

### 🚚 TrackingScreen
- **Card principal:** BlancoPuro, shape 16dp, elevation 4dp
- **Progress bar:**
  - Color en progreso: NaranjaOscuro
  - Color completado: VerdeOscuro
  - Track: GrisSuave
  - Animación: EaseInOutCubic, 1000ms
- **Estados:** Emojis + texto Chocolate Bold 24sp

---

## ✨ Animaciones Implementadas

| Pantalla | Animación | Detalles |
|----------|-----------|----------|
| Catálogo | `scaleIn` | Cards aparecen con spring bounce |
| Catálogo | `animateColorAsState` | Botón NaranjaSuave → VerdeOscuro |
| Checkout | `slideInVertically + fadeIn` | Mensajes de validación |
| Tracking | `animateFloatAsState` | Barra de progreso suave |

---

## 🧁 Coherencia con Proyecto React

✅ **Misma paleta de colores**  
✅ **Misma sensación visual (cálida, artesanal, moderna)**  
✅ **Bordes redondeados coherentes (12dp-16dp)**  
✅ **Transiciones suaves y amigables**  
✅ **Jerarquía tipográfica clara**  
✅ **Espaciado consistente**  

---

## 🚀 Próximos Pasos

1. **Compilar:**
   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

2. **Ejecutar** en emulador o dispositivo

3. **(Opcional) Añadir fuente Poppins** siguiendo instrucciones arriba

4. **(Opcional) Añadir más animaciones:**
   - Transiciones entre pantallas
   - Micro-interacciones en botones
   - Skeleton loading en catálogo

---

## 📝 Archivos Modificados

- `Color.kt` - Paleta completa
- `theme.kt` - Light/Dark schemes
- `Type.kt` - Tipografía escalable
- `CatalogScreen.kt` - Productos con animaciones
- `CartScreen.kt` - Carrito estilizado
- `ProfileScreen.kt` - Formulario completo
- `CheckoutScreen.kt` - Validación animada
- `TrackingScreen.kt` - Progreso animado

---

**Diseñado con 🍰 por el equipo de Pastelería Mil Sabores**

