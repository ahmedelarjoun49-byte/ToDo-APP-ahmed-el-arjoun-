package com.example.todoapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // Modification : On trie d'abord par le score d'importance IA (décroissant), puis par urgence de la deadline
    @Query("SELECT * FROM tasks ORDER BY aiPriorityScore DESC, dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    // --- REQUÊTES POUR L'INTELLIGENCE ET LES STATISTIQUES ---

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasksCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks")
    fun getTotalTasksCount(): Flow<Int>

    // Pour l'analyse des habitudes : Récupère la liste brute des tâches terminées sans Flow (utile dans les Workers)
    @Query("SELECT * FROM tasks WHERE isCompleted = 1")
    suspend fun getCompletedTasksList(): List<Task>

    // Pour l'analyse de l'emploi du temps : Récupère toutes les tâches actives (non terminées)
    @Query("SELECT * FROM tasks WHERE isCompleted = 0")
    suspend fun getActiveTasksList(): List<Task>

    // Pour l'analyse par catégorie (ex: cibler les habitudes de travail ou de sport)
    @Query("SELECT * FROM tasks WHERE category = :category AND isCompleted = 1")
    suspend fun getCompletedTasksByCategory(category: String): List<Task>
}