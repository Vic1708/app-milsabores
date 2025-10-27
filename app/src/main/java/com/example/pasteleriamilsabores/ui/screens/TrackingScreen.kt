package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pasteleriamilsabores.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen() {
    val estados = listOf("Pendiente 📝", "En preparación 👨‍🍳", "En camino 🚚", "Entregado ✅")
    var estadoActual by remember { mutableStateOf(0) }
    val progreso: Float = (estadoActual + 1) / estados.size.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progreso,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOutCubic),
        label = "progress"
    )

    LaunchedEffect(Unit) {
        for (i in estados.indices) {
            estadoActual = i
            delay(2500L)
            if (i == estados.lastIndex) break
        }
    }

    Scaffold(
        containerColor = CremaClaro,
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                color = VerdePastel,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Seguimiento 🚚",
                            color = BlancoPuro,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = VerdePastel,
                        titleContentColor = BlancoPuro
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoPuro),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Estado actual:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MarronSuave,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = estados[estadoActual],
                        fontSize = 24.sp,
                        color = Chocolate,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp),
                        color = if (estadoActual == estados.lastIndex) VerdeOscuro else NaranjaOscuro,
                        trackColor = GrisSuave
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${(animatedProgress * 100).toInt()}% completado",
                        fontSize = 14.sp,
                        color = MarronSuave,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
