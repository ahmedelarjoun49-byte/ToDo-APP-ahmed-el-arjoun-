package com.example.todoapp.ui.tasks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.Task
import com.example.todoapp.data.model.TaskPriority
import com.example.todoapp.ui.components.CategoryChip
import com.example.todoapp.ui.components.NeonCard
import com.example.todoapp.util.HapticHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAnalyticsClick: () -> Unit,
    onFocusClick: (Task) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val haptic = remember { HapticHelper(context) }

    val tasks by viewModel.allTasks.collectAsState()

    var showTaskDialog by remember { mutableStateOf<Task?>(null) }
    var isNewTask by remember { mutableStateOf(true) }

    val categories = listOf("All", "Work", "Personal", "Study", "Health", "Focus")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredTasks =
        if (selectedCategory == "All") tasks
        else tasks.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("My Tasks", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onAnalyticsClick) {
                        Text("Analytics")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showTaskDialog = Task(title = "")
                    isNewTask = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredTasks, key = { it.id }) { task ->

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        when (it) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                viewModel.toggleTaskCompletion(task)
                                false
                            }
                            SwipeToDismissBoxValue.EndToStart -> {
                                viewModel.deleteTask(task)
                                true
                            }
                            else -> false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray)
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                ) {
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTaskCompletion(task) },
                        onEdit = {
                            showTaskDialog = task
                            isNewTask = false
                        },
                        onDelete = { viewModel.deleteTask(task) },
                        onFocus = { onFocusClick(task) }
                    )
                }
            }
        }
    }

    if (showTaskDialog != null) {
        TaskActionDialog(
            task = showTaskDialog!!,
            isNewTask = isNewTask,
            onDismiss = { showTaskDialog = null },
            onConfirm = {
                if (isNewTask) viewModel.addTask(it)
                else viewModel.updateTask(it)

                showTaskDialog = null
            }
        )
    }
}

/* ---------------- TASK ITEM ---------------- */

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onFocus: () -> Unit
) {
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(tween(300))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {

            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (task.isCompleted)
                        Icons.Default.CheckCircle
                    else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (task.isCompleted)
                        TextDecoration.LineThrough else null
                )

                if (task.description.isNotEmpty()) {
                    Text(text = task.description)
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, null)
                }
                IconButton(onClick = onFocus) {
                    Icon(Icons.Default.Timer, null)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, null)
                }
            }
        }
    }
}

/* ---------------- DIALOG ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskActionDialog(
    task: Task,
    isNewTask: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var desc by remember { mutableStateOf(task.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNewTask) "New Task" else "Edit Task") },
        text = {
            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(
                            task.copy(
                                title = title,
                                description = desc
                            )
                        )
                    }
                }
            ) {
                Text(if (isNewTask) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}