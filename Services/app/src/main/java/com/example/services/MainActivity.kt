package com.example.services

import android.Manifest
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "ServicesDemo"
    private var boundService: MyBoundService? = null
    private var isBound = false

    // ServiceConnection for binding to BoundService
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyBoundService.LocalBinder
            boundService = binder.getService()
            isBound = true
            Log.d(TAG, "Bound to MyBoundService")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            Log.d(TAG, "Unbound from MyBoundService")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Sample Notification
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, "ForegroundServiceChannel")
            .setSmallIcon(R.drawable.baseline_notifications)
            .setContentTitle("Test Notification")
            .setContentText("If this appears, channel and permission are fine.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        manager.notify(1002, builder.build())


        //Request it at runtime:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }


        // Start Foreground Service
        binding.btnStartForeground.setOnClickListener {
            val intent = Intent(this, MyForegroundService::class.java)
            startForegroundService(intent)
            Log.d(TAG, "Starting Foreground Service")
        }

        // Stop Foreground Service
        binding.btnStopForeground.setOnClickListener {
            val intent = Intent(this, MyForegroundService::class.java)
            stopService(intent)
            Log.d(TAG, "Stopping Foreground Service")
        }

        // Start Background Service
        binding.btnStartBackground.setOnClickListener {
            val intent = Intent(this, MyBackgroundService::class.java)
            startService(intent)
            Log.d(TAG, "Starting Background Service")
        }

        // Stop Background Service
        binding.btnStopBackground.setOnClickListener {
            val intent = Intent(this, MyBackgroundService::class.java)
            stopService(intent)
            Log.d(TAG, "Stopping Background Service")
        }

        // Bind to Bound Service
        binding.btnBindService.setOnClickListener {
            val intent = Intent(this, MyBoundService::class.java)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
            Log.d(TAG, "Binding to Bound Service")
        }

        // Unbind from Bound Service
        binding.btnUnbindService.setOnClickListener {
            if (isBound) {
                unbindService(connection)
                isBound = false
                Log.d(TAG, "Unbinding from Bound Service")
            }
        }

        // Get data from Bound Service
        binding.btnGetBoundData.setOnClickListener {
            if (isBound) {
                val data = boundService?.getData()
                binding.textView.text = data ?: "Not bound"
                Log.d(TAG, "Fetched data from Bound Service: $data")
            } else {
                binding.textView.text = "Service not bound"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }
}