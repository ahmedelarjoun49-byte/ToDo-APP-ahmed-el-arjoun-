package com.example.todoapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/* ---------------- COLORS ---------------- */

val NeonBlue = Color(0xFF3B82F6)
val NeonCyan = Color(0xFF22D3EE)
val NeonGlow = Color(0xFF60A5FA)

/* ---------------- DESIGN TOKENS ---------------- */

private val CornerSmall = 12.dp
private val CornerMedium = 16.dp
private val CornerLarge = 22.dp

/* ---------------- NEON BUTTON ---------------- */

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = NeonBlue
) {
    val transition = rememberInfiniteTransition(label = "button_glow")

    val glow by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "glow"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(CornerMedium),
        color = color,
        modifier = modifier.shadow(
            (10 * glow).dp,
            RoundedCornerShape(CornerMedium),
            spotColor = color,
            ambientColor = color
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(color, NeonCyan.copy(alpha = 0.8f))
                    )
                )
                .padding(vertical = 14.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/* ---------------- NEON CARD ---------------- */

@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(CornerLarge))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            NeonBlue.copy(alpha = 0.4f),
                            NeonCyan.copy(alpha = 0.2f)
                        )
                    )
                ),
                RoundedCornerShape(CornerLarge)
            )
            .padding(16.dp)
    ) {
        Column(content = content)
    }
}

/* ---------------- GLASS INPUT ---------------- */

@Composable
fun GlassInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
                color = Color.Gray.copy(alpha = 0.7f)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CornerMedium)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = NeonBlue,
            focusedIndicatorColor = NeonBlue,
            unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.4f),
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

/* ---------------- CATEGORY CHIP ---------------- */

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (isSelected) NeonBlue else MaterialTheme.colorScheme.surface,
        border = if (isSelected) null else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/* ---------------- GLOW EFFECT ---------------- */

@Composable
fun Modifier.pulsingGlow(
    color: Color = NeonBlue,
    enabled: Boolean = true
): Modifier {
    if (!enabled) return this

    val transition = rememberInfiniteTransition(label = "glow")

    val alpha by transition.animateFloat(
        0.2f,
        0.7f,
        infiniteRepeatable(
            tween(1400, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val shape = RoundedCornerShape(CornerLarge)

    return this
        .shadow(
            (14 * alpha).dp,
            shape,
            spotColor = color,
            ambientColor = color
        )
        .border(
            1.dp,
            color.copy(alpha = alpha),
            shape
        )
}