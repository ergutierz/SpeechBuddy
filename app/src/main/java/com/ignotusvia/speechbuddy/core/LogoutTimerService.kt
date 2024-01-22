package com.ignotusvia.speechbuddy.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.ignotusvia.speechbuddy.R
import com.ignotusvia.speechbuddy.view.MainActivity

class LogoutTimerService : Service() {

    private val timeoutDuration = 5 * 60 * 1000L // 5 minutes in milliseconds
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            sendLogoutBroadcast()
        }
    }

    private fun sendLogoutBroadcast() {
        val logoutIntent = Intent("com.example.ACTION_LOGOUT")
        sendBroadcast(logoutIntent)
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, "TIMEOUT_CHANNEL_ID")
            .setContentTitle("Session Timeout")
            .setContentText("Your session will expire soon.")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        // rest of your logic
        return START_NOT_STICKY
    }


    private fun createNotificationChannel() {
        val name = "Timeout Channel"
        val descriptionText = "Notifies when user session times out"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("TIMEOUT_CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        const val NOTIFICATION_ID = 1
    }
}
