package com.clogs.disclogs.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

// Extensão do Modifier para podermos usar como .shimmerEffect() em qualquer componente
fun Modifier.shimmerEffect(): Modifier = composed {

    // Transição infinita (um ‘loop’ que nunca para)
    val transition = rememberInfiniteTransition(label = "shimmer_transition")

    // Anima um valor do ponto 0 até 1000 píxel
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // Demora 1 segundo para passar
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart // Quando termina, começa de novo do zero
        ),
        label = "shimmer_translation"
    )

    // Define as cores do "fantasma". Usamos as cores de superfície do seu tema para ficar bom no Dark/Light mode
    val color1 = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    val color2 = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)

    // Cria o pincel de gradiente. A mágica está no 'end = Offset', que se move com a animação
    val brush = Brush.linearGradient(
        colors = listOf(color1, color2, color1),
        start = Offset.Zero,
        end = Offset(x = translateAnimation, y = translateAnimation)
    )

    // Aplica esse gradiente como fundo do componente
    this.background(brush = brush)
}