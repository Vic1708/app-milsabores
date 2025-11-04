package com.example.pasteleriamilsabores.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pasteleriamilsabores.R
import com.example.pasteleriamilsabores.ui.theme.BlancoPuro
import com.example.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme

// 🧁 Modelo de datos para cada slide
data class Slide(
    val imageRes: Int,
    val title: String,
    val subtitle: String,
    val text: String
)

// 🖼️ Lista de slides que se mostrarán en la intro animada
val slides = listOf(


    Slide(
        imageRes = R.drawable.brownie,
        title = "Bienvenido a Mil Sabores",
        subtitle = "Tu pastelería de confianza",
        text = "Descubre una variedad de deliciosos pasteles y postres hechos con amor y los mejores ingredientes."
    ),
    Slide(
        imageRes = R.drawable.hero1,
        title = "Pedidos Personalizados",
        subtitle = "Crea el pastel perfecto",
        text = "Elige entre una amplia gama de sabores, diseños y tamaños para hacer tu pastel único y especial."
    ),
    Slide(
        imageRes = R.drawable.hero2,
        title = "Entrega Rápida",
        subtitle = "Directo a tu puerta",
        text = "Disfruta de la comodidad de recibir tus pedidos en casa con nuestro servicio de entrega rápida y segura."
    )
)

// 🎬 Composable principal que muestra una transición entre slides
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedSlide(index: Int) {
    val safeIndex = if (slides.isEmpty()) 0 else (index % slides.size + slides.size) % slides.size
    val slide = slides.getOrNull(safeIndex) ?: slides.first()

    AnimatedContent(
        targetState = slide,
        transitionSpec = {
            // 👇 Nueva sintaxis para Compose 1.7+
            slideInHorizontally(animationSpec = tween(500)) + fadeIn(animationSpec = tween(400)) togetherWith
                    slideOutHorizontally(animationSpec = tween(400)) + fadeOut(animationSpec = tween(300))
        }
    ) { current ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = current.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                // Imagen redonda tipo ícono


                Surface(
                    modifier = Modifier
                        .size(90.dp)
                        .alpha(0f)
                ) { }

                // Textos del slide
                Text(
                    text = current.title.uppercase(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlancoPuro
                )
                Text(
                    text = current.subtitle.uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = BlancoPuro
                )
                Text(
                    text = current.text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = BlancoPuro
                )
            }
        }
    }
}

// 👀 Preview para Android Studio
@Preview(showBackground = true)
@Composable
fun AnimatedSlidePreview() {
    PasteleriaMilSaboresTheme {
        AnimatedSlide(0)
    }
}
