package com.example.pasteleriamilsabores.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Crea un DataStore en el contexto de la app
val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {

    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PASS = stringPreferencesKey("user_pass")
        private val USER_ADDRESS = stringPreferencesKey("user_address")
        private val USER_BIRTH = stringPreferencesKey("user_birth")
        private val USER_PHONE = stringPreferencesKey("user_phone")
        private val USER_RUT = stringPreferencesKey("user_rut")
        private val USER_AVATAR = stringPreferencesKey("user_avatar")
        private val LOGGED_IN = booleanPreferencesKey("logged_in")
        // Nueva clave para compatibilidad con la versión web
        private val SESSION_USER = stringPreferencesKey("session_user")
    }

    // Guardar datos del usuario (registro completo)
    suspend fun guardarUsuario(
        nombre: String,
        correo: String,
        password: String,
        direccion: String = "",
        fechaNacimiento: String = "",
        telefono: String = "",
        rut: String = "",
        avatar: String = ""
    ) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = nombre
            prefs[USER_EMAIL] = correo
            prefs[USER_PASS] = password
            prefs[USER_ADDRESS] = direccion
            prefs[USER_BIRTH] = fechaNacimiento
            prefs[USER_PHONE] = telefono
            prefs[USER_RUT] = rut
            prefs[USER_AVATAR] = avatar
            prefs[LOGGED_IN] = true
            prefs[SESSION_USER] = correo // guardamos session_user para compatibilidad
        }
    }

    // Guardar solo avatar
    suspend fun guardarAvatar(avatarUri: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_AVATAR] = avatarUri
        }
    }

    // Intento de login: compara email/password con lo guardado
    suspend fun intentarLogin(correo: String, password: String): Boolean {
        val data = context.dataStore.data.first()
        val storedEmail = data[USER_EMAIL]
        val storedPass = data[USER_PASS]
        val ok = (storedEmail != null && storedPass != null && storedEmail == correo && storedPass == password)
        if (ok) {
            context.dataStore.edit { prefs ->
                prefs[LOGGED_IN] = true
                prefs[SESSION_USER] = correo
            }
        }
        return ok
    }

    // Obtener datos del usuario actual como UserEntity (id = 0 en preferences)
    val obtenerUsuario: Flow<UserEntity?> = context.dataStore.data.map { prefs ->
        val email = prefs[USER_EMAIL]
        if (email == null) {
            null
        } else {
            UserEntity(
                id = 0,
                nombre = prefs[USER_NAME] ?: "",
                email = email,
                password = prefs[USER_PASS] ?: "",
                direccion = prefs[USER_ADDRESS] ?: "",
                fechaNacimiento = prefs[USER_BIRTH] ?: "",
                telefono = prefs[USER_PHONE] ?: "",
                rut = prefs[USER_RUT] ?: "",
                avatarUri = prefs[USER_AVATAR]
            )
        }
    }

    // Flow para obtener URI del avatar (string) o null
    val obtenerAvatar: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_AVATAR]
    }

    // Verificar sesión activa
    val sesionActiva: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[LOGGED_IN] ?: false
    }

    // Exponer session_user (clave requerida por la web)
    val sessionUser: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[SESSION_USER]
    }

    // Cerrar sesión
    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
