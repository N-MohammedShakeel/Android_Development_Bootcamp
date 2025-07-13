package com.example.explicitintent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var taskInput: EditText
    private lateinit var viewTaskButton: Button
    private lateinit var resultText: TextView

    // Register Activity Result Launcher for receiving results
    private val taskResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val isComplete = data?.getBooleanExtra("TASK_COMPLETE", false) ?: false
            resultText.text = if (isComplete) "Task marked as complete!" else "Task not completed."
        } else {
            resultText.text = "No result received."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        taskInput = findViewById(R.id.task_input)
        viewTaskButton = findViewById(R.id.view_task_button)
        resultText = findViewById(R.id.result_text)

        // Restore saved input (e.g., after rotation)
        savedInstanceState?.let {
            taskInput.setText(it.getString("task_input"))
        }

        // Set click listener for View Task button
        viewTaskButton.setOnClickListener {
            // Create explicit Intent to start TaskDetailActivity
            val intent = Intent(this, TaskDetailActivity::class.java).apply {
                // Pass task title and ID as extras
                putExtra("TASK_TITLE", taskInput.text.toString())
                putExtra("TASK_ID", 123) // Example static ID
            }
            // Launch the Intent and expect a result
            taskResultLauncher.launch(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save task input for configuration changes
        outState.putString("task_input", taskInput.text.toString())
    }
}
