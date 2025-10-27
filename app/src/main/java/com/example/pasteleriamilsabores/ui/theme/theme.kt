package com.example.pasteleriamilsabores.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = VerdeOscuro,
    onPrimary = BlancoPuro,
    secondary = NaranjaOscuro,
    onSecondary = BlancoPuro,
    tertiary = Chocolate,
    background = Chocolate,
    onBackground = CremaClaro,
    surface = MarronSuave,
    onSurface = CremaClaro
)

private val LightColorScheme = lightColorScheme(
    primary = VerdePastel,
    onPrimary = BlancoPuro,
    secondary = NaranjaSuave,
    onSecondary = BlancoPuro,
    tertiary = Chocolate,
    background = CremaClaro,
    onBackground = Chocolate,
    surface = GrisSuave,
    onSurface = Chocolate
)

@Composable
fun PasteleriaMilSaboresTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
