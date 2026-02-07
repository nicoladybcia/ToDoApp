package com.example.todoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var taskStorage: TaskStorage
    private lateinit var tasks: MutableList<Task>

    private val addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val taskText = result.data?.getStringExtra("newTask")
            val imageUri = result.data?.getStringExtra("imageUri")
            if (taskText != null) {
                tasks.add(Task(taskText, imageUri))
                adapter.notifyItemInserted(tasks.size - 1)
                taskStorage.saveTasks(tasks)
            }
        }
    }

    private val editTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedTask = result.data?.getStringExtra("updatedTask")
            val imageUri = result.data?.getStringExtra("imageUri")
            val position = result.data?.getIntExtra("position", -1) ?: -1
            if (updatedTask != null && position != -1) {
                tasks[position] = Task(updatedTask, imageUri)
                adapter.notifyItemChanged(position)
                taskStorage.saveTasks(tasks)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskStorage = TaskStorage(this)
        tasks = taskStorage.loadTasks()

        recyclerView = findViewById(R.id.recyclerViewTasks)
        val addTaskButton = findViewById<Button>(R.id.buttonAddTask)

        adapter = TaskAdapter(tasks, editTaskLauncher, taskStorage)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTastActivity::class.java)
            addTaskLauncher.launch(intent)
        }
    }
}