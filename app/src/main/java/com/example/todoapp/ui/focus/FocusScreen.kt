package com.example.todoapp.ui.focus

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.data.model.Task
import com.example.todoapp.util.HapticHelper
import kotlinx.coroutines.delay

@Composable
fun FocusScreen(
    task: Task,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val haptic = remember { HapticHelper(context) }

    // ✅ SAFE: extract colorScheme ONCE inside composable
    val colors = MaterialTheme.colorScheme

    var timeLeft by remember { mutableStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }

    val totalTime = 25 * 60f
    val progress = (timeLeft / totalTime).coerceIn(0f, 1f)

    LaunchedEffect(isRunning) {
        if (isRunning) {
            haptic.success()

            while (isRunning && timeLeft > 0) {
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
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = colors.onBackground
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                text = "FOCUS MODE",
                style = MaterialTheme.typography.labelMedium,
                color = colors.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineMedium,
                color = colors.onBackground,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(60.dp))

            Box(contentAlignment = Alignment.Center) {

                // BACKGROUND CIRCLE
                Canvas(modifier = Modifier.size(260.dp)) {
                    drawCircle(
                        color = colors.surfaceVariant,
                        radius = size.minDimension / 2,
                        style = Stroke(width = 12.dp.toPx())
                    )
                }

                // PROGRESS ARC
                Canvas(modifier = Modifier.size(260.dp)) {
                    drawArc(
                        color = colors.primary,
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(
                            width = 12.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                val minutes = timeLeft / 60
                val seconds = timeLeft % 60

                Text(
                    text = "%02d:%02d".format(minutes, seconds),
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Light,
                    color = colors.onBackground
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(if (isRunning) "PAUSE" else "START FOCUS")
            }
        }
    }
}