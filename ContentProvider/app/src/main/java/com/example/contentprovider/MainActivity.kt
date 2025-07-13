package com.example.contentprovider

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.contentprovider.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    private val todosUri = Uri.withAppendedPath(TodoContentProvider.BASE_URI, "todos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add a new task
        binding.btnAddTask.setOnClickListener {
            val task = binding.editTextTask.text.toString()
            if (task.isNotEmpty()) {
                val values = ContentValues().apply {
                    put(TodosDatabaseHelper.COLUMN_TASK, task)
                }
                val newUri = contentResolver.insert(todosUri, values)
                binding.editTextTask.text.clear()
                Log.d(TAG, "Task added: $newUri")
                displayTasks()
            }
        }

        // Update a task
        binding.btnUpdateTask.setOnClickListener {
            val id = binding.editTextTaskId.text.toString()
            val task = binding.editTextTask.text.toString()
            if (id.isNotEmpty() && task.isNotEmpty()) {
                val values = ContentValues().apply {
                    put(TodosDatabaseHelper.COLUMN_TASK, task)
                }
                val uri = Uri.withAppendedPath(todosUri, id)
                val count = contentResolver.update(uri, values, null, null)
                if (count > 0) {
                    Log.d(TAG, "Task $id updated")
                    displayTasks()
                } else {
                    Log.e(TAG, "Failed to update task $id")
                }
            }
        }

        // Delete a task
        binding.btnDeleteTask.setOnClickListener {
            val id = binding.editTextTaskId.text.toString()
            if (id.isNotEmpty()) {
                val uri = Uri.withAppendedPath(todosUri, id)
                val count = contentResolver.delete(uri, null, null)
                if (count > 0) {
                    Log.d(TAG, "Task $id deleted")
                    displayTasks()
                } else {
                    Log.e(TAG, "Failed to delete task $id")
                }
            }
        }

        // Display tasks on start
        displayTasks()
    }

    private fun displayTasks() {
        val cursor = contentResolver.query(todosUri, null, null, null, null)
        val tasks = StringBuilder()
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(TodosDatabaseHelper.COLUMN_ID))
                val task = it.getString(it.getColumnIndexOrThrow(TodosDatabaseHelper.COLUMN_TASK))
                tasks.append("ID: $id, Task: $task\n")
            }
        }
        binding.textViewTasks.text = tasks.toString().ifEmpty { "No tasks" }
    }
}