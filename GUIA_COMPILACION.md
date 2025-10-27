# 🚀 Guía de Compilación y Ejecución

## Pastelería Mil Sabores - App Android

---

## ✅ Estado del Proyecto

**✅ LISTO PARA COMPILAR Y EJECUTAR**

- ✅ Todos los archivos creados
- ✅ Identidad visual implementada
- ✅ Funcionalidades completas
- ✅ Sin errores de compilación
- ✅ Solo advertencias menores (no afectan funcionamiento)

---

## 🔧 Requisitos Previos

### Android Studio
- **Versión:** Arctic Fox o superior
- **Gradle:** 8.0+
- **Kotlin:** 1.9+

### SDK de Android
- **compileSdk:** 34
- **minSdk:** 24 (Android 7.0)
- **targetSdk:** 34

### Dependencias Principales
```gradle
- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel Compose
- Kotlin Coroutines
```

---

## 📥 Paso 1: Abrir el Proyecto

1. **Abrir Android Studio**

2. **Abrir proyecto existente:**
   ```
   File → Open
   Navegar a: C:\Users\Usuario\AndroidStudioProjects\PasteleriaMilSabores
   Seleccionar y abrir
   ```

3. **Esperar sincronización automática de Gradle**
   - Ver barra inferior: "Gradle sync..."
   - Esperar a que termine (puede tardar 1-3 minutos)

---

## 🔄 Paso 2: Sincronizar y Limpiar

### Opción A: Desde menú
```
File → Sync Project with Gradle Files
```

### Opción B: Desde Build
```
Build → Clean Project
(esperar)
Build → Rebuild Project
```

### Si hay error de sync:
1. Verificar conexión a internet
2. Invalidar caché:
   ```
   File → Invalidate Caches / Restart
   → Invalidate and Restart
   ```

---

## 🏗️ Paso 3: Compilar

### Compilar debug APK
```
Build → Make Project
(o presionar Ctrl+F9)
```

### Compilar APK completo
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

**Ubicación del APK:**
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 📱 Paso 4: Configurar Dispositivo

### Opción A: Emulador

1. **Crear emulador:**
   ```
   Tools → Device Manager
   → Create Device
   ```

2. **Seleccionar dispositivo:**
   - Recomendado: Pixel 5 o superior
   - Sistema: Android 13 (API 33) o superior

3. **Iniciar emulador:**
   - Click en ▶️ junto al dispositivo

### Opción B: Dispositivo Físico

1. **Habilitar modo desarrollador** en el dispositivo:
   - Ajustes → Acerca del teléfono
   - Tocar "Número de compilación" 7 veces

2. **Habilitar depuración USB:**
   - Ajustes → Opciones de desarrollador
   - Activar "Depuración USB"

3. **Conectar dispositivo** con cable USB

4. **Autorizar computadora** en el dispositivo

---

## ▶️ Paso 5: Ejecutar

### Método 1: Botón Run
```
1. Seleccionar dispositivo en la barra superior
2. Click en ▶️ Run 'app'
3. (o presionar Shift+F10)
```

### Método 2: Menú
```
Run → Run 'app'
```

### Método 3: Gradle
```
Terminal en Android Studio:
./gradlew installDebug
```

**Tiempo de instalación:** 10-30 segundos

---

## 🎨 Paso 6: Probar la App

### Flujo completo de prueba:

1. **Catálogo (pantalla inicial):**
   - ✅ Ver 3 productos con imágenes
   - ✅ Presionar "Agregar al carrito" (ver animación de color)
   - ✅ Agregar 2-3 productos

2. **Carrito:**
   - ✅ Ir a pestaña "Carrito" (barra inferior)
   - ✅ Ver productos agregados
   - ✅ Ver total calculado
   - ✅ Probar botón "Eliminar" (ícono basura)
   - ✅ Probar botón "Vaciar"

3. **Perfil:**
   - ✅ Ir a pestaña "Perfil"
   - ✅ Llenar formulario:
     - Nombre: "Juan Pérez"
     - RUT: "12345678-9"
     - Correo: "juan@duocuc.cl"
     - Fecha: "1980-05-15"
     - Dirección: "Calle Falsa 123"
   - ✅ Presionar "Calcular descuento"
   - ✅ Ver edad calculada y descuento aplicado

4. **Checkout:**
   - ✅ Volver a Carrito, agregar productos
   - ✅ Presionar "Checkout"
   - ✅ Llenar formulario de envío
   - ✅ Presionar "Confirmar pedido"
   - ✅ Ver mensaje de éxito animado

5. **Seguimiento:**
   - ✅ Ir a pestaña "Seguimiento"
   - ✅ Ver animación automática de estados
   - ✅ Ver barra de progreso animada
   - ✅ Ver cambio de color al completar

---

## 🐛 Solución de Problemas

### Error: "Gradle sync failed"
**Solución:**
```
File → Invalidate Caches / Restart
File → Sync Project with Gradle Files
```

### Error: "SDK not found"
**Solución:**
```
File → Project Structure → SDK Location
Verificar Android SDK Location
Si está vacío, click en "Download" para instalar SDK
```

### Error: "Build failed"
**Solución:**
```
Build → Clean Project
(esperar)
Build → Rebuild Project
```

### Error: "Device not found"
**Solución:**
- Emulador: Iniciar emulador primero
- Físico: Verificar cable USB, autorizar computadora

### App se cierra inmediatamente
**Solución:**
```
Ver Logcat (ventana inferior):
View → Tool Windows → Logcat
Buscar líneas rojas con "Exception"
Copiar error y revisar
```

### Error: "java.time requires API 26+"
**Solución:**
Ya implementado con `@RequiresApi(Build.VERSION_CODES.O)`
Si aparece error, verificar que minSdk >= 24 en build.gradle

---

## 📊 Verificar Compilación Exitosa

### Señales de éxito:

✅ **En Build Output:**
```
BUILD SUCCESSFUL in 30s
```

✅ **En Run:**
```
Installing APK...
Launching 'app' on Device
```

✅ **En Logcat:**
```
D/MainActivity: onCreate
```

✅ **En dispositivo:**
- App se abre
- Ver pantalla de catálogo con productos
- Colores correctos (verde, naranja, crema)

---

## 📈 Rendimiento Esperado

- **Tiempo de compilación inicial:** 1-3 minutos
- **Compilaciones incrementales:** 10-30 segundos
- **Tiempo de instalación:** 10-30 segundos
- **Tiempo de inicio de app:** < 2 segundos

---

## 📝 Generar APK para Compartir

### Debug APK (desarrollo):
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
Ubicación: app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (producción):
```
Build → Generate Signed Bundle / APK
→ APK
→ Create new keystore...
→ Completar datos
→ Build
```

---

## 🎯 Checklist Final

Antes de la defensa, verificar:

- [ ] App compila sin errores
- [ ] App se ejecuta en emulador/dispositivo
- [ ] Todas las pantallas son accesibles
- [ ] Botones funcionan correctamente
- [ ] Animaciones se ven suaves
- [ ] Colores son coherentes
- [ ] Carrito mantiene productos agregados
- [ ] Cálculo de edad funciona
- [ ] Validaciones muestran mensajes
- [ ] Seguimiento se anima automáticamente

---

## 📞 Soporte

Si encuentras problemas:

1. **Ver Logcat** para errores específicos
2. **Limpiar proyecto** (Clean + Rebuild)
3. **Invalidar caché** de Android Studio
4. **Reiniciar Android Studio**
5. **Verificar versiones** de Gradle/SDK

---

## ✨ ¡Listo!

Tu aplicación **Pastelería Mil Sabores** está lista para:
- ✅ Compilar
- ✅ Ejecutar
- ✅ Probar
- ✅ Defender

**¡Mucha suerte en tu evaluación! 🍰**

---

**Última actualización:** 2025-10-26

