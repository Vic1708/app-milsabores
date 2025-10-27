package com.example.pasteleriamilsabores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pasteleriamilsabores.ui.screens.AppNavigation
import com.example.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasteleriaMilSaboresTheme {
                AppNavigation()
            }
        }
    }
}
