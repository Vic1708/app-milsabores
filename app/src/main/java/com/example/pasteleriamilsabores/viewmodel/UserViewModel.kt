package com.example.pasteleriamilsabores.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilsabores.data.local.UserEntity
import com.example.pasteleriamilsabores.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegisterResult {
    object Success : RegisterResult()
    object AlreadyExists : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult

    fun clearRegisterResult() {
        _registerResult.value = null
    }

    fun registerUser(
        nombre: String,
        email: String,
        password: String,
        confirmPassword: String,
        fechaNacimiento: String,
        direccion: String,
        rut: String,
        telefono: String
    ) {
        // Validaciones del lado del ViewModel
        if (nombre.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() || rut.isBlank()) {
            _registerResult.value = RegisterResult.Error("Por favor completa todos los campos obligatorios")
            return
        }

        // nombre completo: debe tener más de 3 caracteres
        if (nombre.trim().length <= 3) {
            _registerResult.value = RegisterResult.Error("El nombre debe tener más de 3 caracteres")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.contains("@")) {
            _registerResult.value = RegisterResult.Error("Correo no válido")
            return
        }

        if (password.length < 6) {
            _registerResult.value = RegisterResult.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (password != confirmPassword) {
            _registerResult.value = RegisterResult.Error("Las contraseñas deben coincidir")
            return
        }

        // RUT: exactamente 9 dígitos
        if (!rut.matches(Regex("^\\d{9}$"))) {
            _registerResult.value = RegisterResult.Error("RUT inválido: debe contener 9 dígitos sin puntos ni guion")
            return
        }

        // Teléfono opcional: si se entrega, validar que sean dígitos y longitud razonable
        if (telefono.isNotBlank() && !telefono.matches(Regex("^\\d{6,15}$"))) {
            _registerResult.value = RegisterResult.Error("Teléfono inválido")
            return
        }

        // Si valida, guardar en Room (suspend en coroutine)
        viewModelScope.launch {
            val user = UserEntity(
                nombre = nombre,
                email = email,
                password = password,
                fechaNacimiento = fechaNacimiento,
                direccion = direccion,
                telefono = telefono,
                rut = rut
            )

            val id = repository.register(user)
            if (id == -1L) {
                _registerResult.value = RegisterResult.AlreadyExists
            } else {
                _registerResult.value = RegisterResult.Success
            }
        }
    }
}
