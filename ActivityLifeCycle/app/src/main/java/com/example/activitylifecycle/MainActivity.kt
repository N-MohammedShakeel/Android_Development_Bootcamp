package com.example.activitylifecycle

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.activitylifecycle.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    // View Binding instance to safely access UI elements
    private lateinit var binding: ActivityMainBinding
    // Tag for Logcat to filter lifecycle logs
    private val TAG = "MSActivityLifecycle"

    // Called when the activity is first created
    // This is where you initialize your UI, set up views, and restore saved state
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Set the content view to the root of the layout
        setContentView(binding.root)
        // Update TextView to display a message
        binding.text1.text = "Activity Lifecycle Demo"
        // Log the event for debugging
        Log.d(TAG, "onCreate called: Activity is created, initializing UI and resources")
    }

    // Called when the activity becomes visible to the user
    // The activity is not yet interactive; use for lightweight setup
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called: Activity is now visible, preparing to be interactive")
    }

    // Called when the activity is in the foreground and interactive
    // This is where the activity handles user input and active operations
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called: Activity is fully interactive, ready for user input")
    }

    // Called when the activity is partially obscured (e.g., dialog appears)
    // Save transient state and pause ongoing actions like animations
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called: Activity is partially obscured, saving transient state")
    }

    // Called when the activity is no longer visible
    // Release heavy resources (e.g., network connections) and save data
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called: Activity is hidden, releasing resources")
    }

    // Called when the activity is restarting after being stopped
    // Use for reinitializing resources before onStart
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart called: Activity is restarting, preparing to be visible again")
    }

    // Called when the activity is being destroyed
    // Clean up resources (e.g., unregister listeners) to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called: Activity is destroyed, cleaning up resources")
    }
}