package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    companion object{
        const val NOTIFICATION_ID =  1
        var CHANNEL_ID = "channel_01"
        var CHANNEL_NAME:CharSequence = "Emilia-tan channel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24,20)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_notifications_24))
                .setContentTitle(resources.getString(R.string.content_title))
                .setContentText(resources.getString(R.string.content_text))
                .setSubText(resources.getString(R.string.subtext))
                .setAutoCancel(true)

        /* ================================================================
         * Untuk Android Oreo Keatas perlu / Harus menambahkan Notification Channel
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME as String?
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
            Toast.makeText(this,"Masuk IF ", Toast.LENGTH_SHORT).show()
        }
        //=============================================================

        val notification = mBuilder.build()

        mNotificationManager.notify(NOTIFICATION_ID,notification)
        //Kode di atas digunakan untuk mengirim notifikasi sesuai dengan id yang kita berikan.
        // Fungsi id di sini nanti juga bisa untuk membatalkan notifikasi yang sudah muncul.
    }
}