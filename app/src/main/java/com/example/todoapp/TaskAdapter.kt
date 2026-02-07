package com.example.todoapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val editTaskLauncher: ActivityResultLauncher<Intent>,
    private val taskStorage: TaskStorage
) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTask: TextView = itemView.findViewById(R.id.textTask)
        val imageTask: ImageView = itemView.findViewById(R.id.imageTask)
        val editButton: ImageButton = itemView.findViewById(R.id.buttonEdit)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.textTask.text = task.text
        task.imageUri?.let {
            holder.imageTask.setImageURI(Uri.parse(it))
        } ?: holder.imageTask.setImageResource(R.mipmap.ic_launcher)

        holder.editButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditTaskActivity::class.java)
            intent.putExtra("task", task.text)
            intent.putExtra("imageUri", task.imageUri)
            intent.putExtra("position", holder.adapterPosition)
            editTaskLauncher.launch(intent)
        }

        holder.deleteButton.setOnClickListener {
            val adapterPos = holder.adapterPosition
            if (adapterPos != RecyclerView.NO_POSITION) {
                tasks.removeAt(adapterPos)
                notifyItemRemoved(adapterPos)
                taskStorage.saveTasks(tasks)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size
}