package com.vibelocal.app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vibelocal.app.R

class VibeLocalMessagingService: FirebaseMessagingService(){override fun onMessageReceived(m:RemoteMessage){val manager=getSystemService(NotificationManager::class.java);if(Build.VERSION.SDK_INT>=26)manager.createNotificationChannel(NotificationChannel("vibelocal","VibeLocal",NotificationManager.IMPORTANCE_DEFAULT));val n=NotificationCompat.Builder(this,"vibelocal").setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(m.notification?.title?:"VibeLocal").setContentText(m.notification?.body?:"You have an event update.").setAutoCancel(true).build();manager.notify((System.currentTimeMillis()%100000).toInt(),n)}}
