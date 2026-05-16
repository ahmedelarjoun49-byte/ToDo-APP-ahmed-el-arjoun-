package com.example.todoapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.todoapp.MainActivity

class NotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        // Modification : Identifiants mis à jour pour coller au thème d'assistant intelligent
        const val CHANNEL_ID = "smart_ai_reminders"
        const val CHANNEL_NAME = "Assistant IA & Emploi du Temps"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alertes prédictives de l'IA pour la gestion quotidienne et les deadlines."
                enableLights(true)
                // Optionnel : On peut forcer une couleur de LED bleue sur les appareils compatibles
                lightColor = 0xFF0072FF.toInt()
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Affiche une notification système stylisée par l'assistant intelligent.
     */
    fun showNotification(title: String, message: String, taskId: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Optionnel : On passe l'ID de la tâche à la MainActivity au cas où on veut ouvrir directement le focus
            putExtra("TASK_ID", taskId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Modification : Construction d'une notification plus riche et immersive
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder) // Icone plus adaptée pour un rappel urgent
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // Permet d'afficher les longs messages d'analyse de l'IA
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            // Couleur bleue électrique pour l'habillage de la notification dans le volet Android
            .setColor(0xFF0072FF.toInt())
            .setColorized(true)
            .build()

        notificationManager.notify(taskId.hashCode(), notification)
    }
}