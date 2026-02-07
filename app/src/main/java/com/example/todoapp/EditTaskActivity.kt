package com.example.todoapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class EditTaskActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Take persistable URI permission
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            contentResolver.takePersistableUriPermission(it, flag)
            imageUri = it
            findViewById<ImageView>(R.id.imagePreview).setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        val editTask = findViewById<EditText>(R.id.editTask)
        val buttonSelectImage = findViewById<Button>(R.id.buttonSelectImage)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        val originalTask = intent.getStringExtra("task")
        val originalImageUri = intent.getStringExtra("imageUri")
        val taskPosition = intent.getIntExtra("position", -1)

        editTask.setText(originalTask)
        originalImageUri?.let {
            imageUri = Uri.parse(it)
            findViewById<ImageView>(R.id.imagePreview).setImageURI(imageUri)
        }

        buttonSelectImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        buttonSave.setOnClickListener {
            val updatedTask = editTask.text.toString()
            if (updatedTask.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("updatedTask", updatedTask)
                resultIntent.putExtra("position", taskPosition)
                imageUri?.let { resultIntent.putExtra("imageUri", it.toString()) }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}