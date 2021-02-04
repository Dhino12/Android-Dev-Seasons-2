package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    }

    //aksi untuk onClick pada button
    //Digunakan untuk memanggil fungsi langsung dari layout, syaratnya yaitu fungsi tersebut memiliki parameter View.
    fun sendNotification(view:View){
        //PendingIntent untuk diterapkan di mBuilder pada MainActivity seperti di bawah ini:
        /*
        * Digunakan untuk memberikan action jika notifikasi disentuh. Selain ke halaman Web,
        *  anda juga bisa mengganti intent tersebut ke Activity dengan menggunakan Intent untuk memanggil activity seperti biasanya.
        * */
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://dicoding.com"))
        val pandingIntent = PendingIntent.getActivity(this,0,intent,0)
        //==========================================================================================
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pandingIntent) // Panding Intent
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
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME as String?
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
            Toast.makeText(this,"Masuk IF ",Toast.LENGTH_SHORT).show()
        }
        //=============================================================

        val notification = mBuilder.build()

        mNotificationManager.notify(NOTIFICATION_ID,notification)
        //Kode di atas digunakan untuk mengirim notifikasi sesuai dengan id yang kita berikan.
        // Fungsi id di sini nanti juga bisa untuk membatalkan notifikasi yang sudah muncul.
    }
}