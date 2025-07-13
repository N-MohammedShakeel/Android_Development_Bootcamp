package com.example.explicitintent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var taskTitleText: TextView
    private lateinit var taskIdText: TextView
    private lateinit var completeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        // Initialize UI elements
        taskTitleText = findViewById(R.id.task_title_text)
        taskIdText = findViewById(R.id.task_id_text)
        completeButton = findViewById(R.id.complete_button)

        // Retrieve data from Intent
        val taskTitle = intent.getStringExtra("TASK_TITLE") ?: "No Title"
        val taskId = intent.getIntExtra("TASK_ID", 0)

        // Display task data
        taskTitleText.text = "Task: $taskTitle"
        taskIdText.text = "ID: $taskId"

        // Set click listener for Complete button
        completeButton.setOnClickListener {
            // Create result Intent
            val resultIntent = Intent().apply {
                putExtra("TASK_COMPLETE", true)
            }
            // Set result and finish Activity
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
