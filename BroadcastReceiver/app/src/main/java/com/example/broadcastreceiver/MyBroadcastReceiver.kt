package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "MyBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                Log.d(TAG, "Power connected: Device is charging")
                Toast.makeText(context, "Power Connected!", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                Log.d(TAG, "Power disconnected: Device is not charging")
                Toast.makeText(context, "Power Disconnected!", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                Log.d(TAG, "AIRPLANE MODE CHANGED")
                Toast.makeText(context, "AIRPLANE MODE!", Toast.LENGTH_SHORT).show()
            }
            "com.example.broadcastreceiverdemo.CUSTOM_BROADCAST" -> {
                val message = intent.getStringExtra("message") ?: "No message"
                Log.d(TAG, "Custom broadcast received: $message")
                Toast.makeText(context, "Custom Broadcast: $message", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Log.d(TAG, "Unknown broadcast received: ${intent?.action}")
            }
        }
    }
}