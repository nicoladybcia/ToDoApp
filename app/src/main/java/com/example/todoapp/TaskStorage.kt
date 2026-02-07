package com.example.todoapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskStorage(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTasks(tasks: List<Task>) {
        val json = gson.toJson(tasks)
        sharedPreferences.edit().putString("taskList", json).apply()
    }

    fun loadTasks(): MutableList<Task> {
        val json = sharedPreferences.getString("taskList", null)
        val type = object : TypeToken<MutableList<Task>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }
}