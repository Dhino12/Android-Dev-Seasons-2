package com.example.myapplication

import android.app.IntentService
import android.content.Intent
import android.util.Log

class DownloadService : IntentService("DownloadService") {


    companion object{
        val TAG = DownloadService::class.java.simpleName
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG,"Download Service diJalankan")
        if(intent != null){
            try {
                Thread.sleep(5000)
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }
        //Pada kenyataanya, DownloadService ini hanya melakukan proses sleep() selama 5 detik
        // dan kemudian mem-broadcast sebuah IntentFilter dengan
        // Action yang telah ditentukan, ACTION_DOWNLOAD_STATUS.
        //Ketika baris ini dijalankan pada DownloadService.
        val notifFinishIntent = Intent(MainActivity.ACTION_DOWNLOAD_STATUS)
        sendBroadcast(notifFinishIntent)
        //Maka seketika itu pula metode onReceive() di bawah ini akan merespon untuk melakukan proses di dalamnya:
    }
}
