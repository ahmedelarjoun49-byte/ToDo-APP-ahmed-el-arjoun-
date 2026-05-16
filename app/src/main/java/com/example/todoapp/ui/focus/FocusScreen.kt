package com.example.todoapp.ui.focus

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.data.model.Task
import com.example.todoapp.ui.theme.*
import com.example.todoapp.ui.components.NeonButton // Importation de ton nouveau bouton bleu
import com.example.todoapp.ui.components.SmoothNeonBlue
import com.example.todoapp.ui.components.DeepBlueGlow
import kotlinx.coroutines.delay
import com.example.todoapp.util.HapticHelper

@Composable
fun FocusScreen(
    task: Task,
    onClose: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val haptic = remember { HapticHelper(context) }
    var timeLeft by remember { mutableStateOf(25 * 60) } // 25 minutes default
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            haptic.success()
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isRunning = false
        } else {
            haptic.click()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(32.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "FOCUSING ON",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(64.dp))

            Box(contentAlignment = Alignment.Center) {
                // Background Circle (Le cercle de fond sombre de la jauge)
                Canvas(modifier = Modifier.size(280.dp)) {
                    drawCircle(
                        color = SurfaceDark,
                        radius = size.minDimension / 2,
                        style = Stroke(width = 12.dp.toPx())
                    )
                }

                // Progress Arc (Modification : Changé en Bleu Néon avec effet dynamique)
                val progress = timeLeft / (25 * 60f)
                Canvas(modifier = Modifier.size(280.dp)) {
                    drawArc(
                        color = SmoothNeonBlue,
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val minutes = timeLeft / 60
                    val seconds = timeLeft % 60
                    Text(
                        text = "%02d:%02d".format(minutes, seconds),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        fontSize = 72.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Modification : Remplacement du bouton classique par notre NeonButton personnalisé bleu électrique
            NeonButton(
                text = if (isRunning) "PAUSE" else "START FOCUS",
                onClick = { isRunning = !isRunning },
                containerColor = if (isRunning) SurfaceDark else SmoothNeonBlue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }
    }
}