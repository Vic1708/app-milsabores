package com.example.pasteleriamilsabores.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.local.UserEntity
import com.example.pasteleriamilsabores.data.local.UserPreferences
import com.example.pasteleriamilsabores.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val prefs: UserPreferences, private val userRepository: UserRepository) : ViewModel() {

    val usuario: StateFlow<UserEntity?> = prefs.obtenerUsuario
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val sesionActiva: StateFlow<Boolean> = prefs.sesionActiva
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // Exponer sessionUser para compatibilidad con la web
    val sessionUser: StateFlow<String?> = prefs.sessionUser
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // Exponer avatar (URI string) para que la UI pueda mostrar avatar del usuario
    val avatar: StateFlow<String?> = prefs.obtenerAvatar
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun registrarUsuario(
        nombre: String,
        correo: String,
        password: String,
        direccion: String = "",
        fechaNacimiento: String = "",
        telefono: String = "",
        rut: String = ""
    ) {
        viewModelScope.launch {
            prefs.guardarUsuario(nombre, correo, password, direccion, fechaNacimiento, telefono, rut)
        }
    }

    // Nuevo: guardar avatar a través del ViewModel para desacoplar la UI
    fun guardarAvatar(uriString: String) {
        viewModelScope.launch {
            prefs.guardarAvatar(uriString)
        }
    }

    /**
     * Intenta hacer login validando contra Room Database (múltiples usuarios).
     * Si es exitoso, guarda la sesión en DataStore.
     */
    suspend fun intentarLogin(correo: String, password: String): Boolean {
        return try {
            // Buscar usuario en Room
            val user = userRepository.login(correo, password)
            if (user != null) {
                // Guardar sesión en DataStore
                prefs.guardarUsuario(
                    nombre = user.nombre,
                    correo = user.email,
                    password = user.password,
                    direccion = user.direccion,
                    fechaNacimiento = user.fechaNacimiento,
                    telefono = user.telefono,
                    rut = user.rut
                )
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        viewModelScope.launch { prefs.cerrarSesion() }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                val prefs = UserPreferences(context)
                val repository = UserRepository(context)
                return AuthViewModel(prefs, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
