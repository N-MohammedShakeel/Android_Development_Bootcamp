package com.example.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyBackgroundService : Service() {

    private val TAG = "MyBackgroundService"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Background Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: Background Service started")
        // Simulate a short background task
        Thread {
            try {
                Thread.sleep(10000) // Simulate 10 seconds of work
                Log.d(TAG, "Background Service work completed")
                stopSelf()
            } catch (e: InterruptedException) {
                Log.e(TAG, "Background Service interrupted")
            }
        }.start()
        return START_NOT_STICKY // Service does not restart if killed
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Background Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Not a bound service
    }
}