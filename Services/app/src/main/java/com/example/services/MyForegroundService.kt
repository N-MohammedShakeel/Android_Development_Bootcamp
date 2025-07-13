package com.example.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

    private val TAG = "MyForegroundService"
    private val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Foreground Service created")
        createNotificationChannel()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: Foreground Service started")
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Running in the foreground")
            .setSmallIcon(R.drawable.baseline_notifications)
            .build()
        startForeground(1, notification)
        // Simulate ongoing work
        Thread {
            try {
                Thread.sleep(20000) // Simulate 20 sec of work
                Log.d(TAG, "Foreground Service work completed")
                stopSelf()
            } catch (e: InterruptedException) {
                Log.e(TAG, "Foreground Service interrupted")
            }
        }.start()
        return START_STICKY // Service restarts if killed
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Foreground Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Not a bound service
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.setShowBadge(true)

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

    }
}