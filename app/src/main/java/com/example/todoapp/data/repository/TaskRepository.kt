package com.example.todoapp.data.repository

import com.example.todoapp.data.local.TaskDao
import com.example.todoapp.data.model.Task
import com.example.todoapp.data.model.TaskPriority
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    val completedCount: Flow<Int> = taskDao.getCompletedTasksCount()
    val totalCount: Flow<Int> = taskDao.getTotalTasksCount()

    // Modification : Calcule le score IA avant d'insérer la tâche
    suspend fun insert(task: Task) {
        val smartTask = currentCalculateAiScore(task)
        taskDao.insertTask(smartTask)
    }

    // Modification : Recalcule le score IA lors d'une mise à jour (si l'heure ou le statut change)
    suspend fun update(task: Task) {
        val smartTask = if (!task.isCompleted) currentCalculateAiScore(task) else task
        taskDao.updateTask(smartTask)
    }

    suspend fun delete(task: Task) = taskDao.deleteTask(task)

    // --- LOGIQUE INTELLIGENTE : MOTEUR DE PRIORISATION IA ---

    /**
     * Analyse les paramètres de la tâche et les habitudes de l'utilisateur
     * pour générer un score de priorité prédictif dynamique.
     */
    private suspend fun currentCalculateAiScore(task: Task): Task {
        var score = 0.0f

        // 1. Prise en compte de la priorité utilisateur (Poids : 30%)
        score += when (task.priority) {
            TaskPriority.HIGH -> 30.0f
            TaskPriority.MEDIUM -> 15.0f
            TaskPriority.LOW -> 5.0f
        }

        // 2. Analyse de l'urgence / Emploi du temps (Poids : 40%)
        task.dueDate?.let { deadline ->
            val timeLeftMs = deadline - System.currentTimeMillis()
            if (timeLeftMs > 0) {
                val hoursLeft = timeLeftMs / (1000 * 60 * 60)
                score += when {
                    hoursLeft <= 2 -> 40.0f  // Critique : moins de 2 heures
                    hoursLeft <= 6 -> 30.0f  // Très urgent : moins de 6 heures
                    hoursLeft <= 24 -> 20.0f // Urgent : dans la journée
                    hoursLeft <= 72 -> 10.0f // Modéré : dans les 3 jours
                    else -> 2.0f
                }
            } else {
                // La tâche a dépassé la date limite (En retard !)
                score += 45.0f
            }
        }

        // 3. Analyse prédictive des habitudes de l'utilisateur (Poids : 30%)
        // On regarde l'historique des tâches de la même catégorie déjà complétées
        val completedHistory = taskDao.getCompletedTasksByCategory(task.category)
        if (completedHistory.isNotEmpty()) {
            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            var matchingHabitsCount = 0

            for (historicalTask in completedHistory) {
                historicalTask.completedAt?.let { compTime ->
                    val historyCal = Calendar.getInstance().apply { timeInMillis = compTime }
                    val historyHour = historyCal.get(Calendar.HOUR_OF_DAY)

                    // Si l'utilisateur termine souvent cette catégorie de tâches à +/- 2h de l'heure actuelle
                    if (Math.abs(historyHour - currentHour) <= 2) {
                        matchingHabitsCount++
                    }
                }
            }

            // Si une habitude forte se dégage, on booste le score pour lui proposer la tâche au bon moment
            if (matchingHabitsCount > 0) {
                score += Math.min(matchingHabitsCount * 5.0f, 30.0f)
            }
        }

        return task.copy(aiPriorityScore = score)
    }

    /**
     * Permet au Worker de recalculer en arrière-plan les scores de toutes les tâches
     * en cours pour actualiser l'ordre de la To-Do list selon l'heure de la journée.
     */
    suspend fun refreshAllCalculatedPriorities() {
        val activeTasks = taskDao.getActiveTasksList()
        for (task in activeTasks) {
            val updatedTask = currentCalculateAiScore(task)
            taskDao.updateTask(updatedTask)
        }
    }
}