package com.example.broadcastreceiver

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.broadcastreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: MyBroadcastReceiver
    private val TAG = "MainActivity"

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize BroadcastReceiver
        receiver = MyBroadcastReceiver()

        // Register receiver dynamically for system broadcasts
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction("com.example.broadcastreceiverdemo.CUSTOM_BROADCAST")
        }
        registerReceiver(receiver, filter)

        // Button to send custom broadcast
        binding.btnSendBroadcast.setOnClickListener {
            val intent = Intent("com.example.broadcastreceiverdemo.CUSTOM_BROADCAST")
            intent.putExtra("message", "Hello from Custom Broadcast!")
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister receiver to prevent leaks
        unregisterReceiver(receiver)
    }
}