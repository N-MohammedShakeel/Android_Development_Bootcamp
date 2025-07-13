package com.example.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyBoundService : Service() {

    private val TAG = "MyBoundService"
    private val binder = LocalBinder()

    // Binder to allow clients to access the service
    inner class LocalBinder : Binder() {
        fun getService(): MyBoundService = this@MyBoundService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Bound Service created")
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind: Client bound to service")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: All clients unbound")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Bound Service destroyed")
    }

    // Example method clients can call
    fun getData(): String {
        return "Data from Bound Service: ${System.currentTimeMillis()}"
    }
}