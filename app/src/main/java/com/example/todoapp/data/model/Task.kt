package com.example.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class TaskPriority {
    LOW, MEDIUM, HIGH
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val category: String = "General", // Ex: Travail, Études, Sport, Courses
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: Long? = null, // Horodatage (Timestamp) de l'échéance
    val isCompleted: Boolean = false,
    val completedAt: Long? = null, // Utile pour analyser à quelle heure/jour l'utilisateur termine ses tâches
    val createdAt: Long = System.currentTimeMillis(),

    // --- AJOUT POUR L'INTELLIGENCE ARTIFICIELLE / GESTION INTELLIGENTE ---
    // Ce score sera calculé automatiquement par notre algorithme de tri intelligent.
    // Plus le score est élevé, plus la tâche montera automatiquement en haut de la To-Do list.
    val aiPriorityScore: Float = 0.0f
)