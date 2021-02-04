package com.example.myapplication

import android.app.IntentService
import android.content.Intent
import android.util.Log

class MyIntentService : IntentService("MyIntentService") {

    companion object{
        internal const val EXTRA_DURATION = "extra_duration"
        private val TAG = MyIntentService::class.java.simpleName
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG,"onHandleIntent: MULAI.....")
        val duration = intent?.getLongExtra(EXTRA_DURATION,0) as Long

        try {
            Thread.sleep(duration)
            Log.d(TAG,"onHandleIntent: Selesai.....")
        }catch (e:InterruptedException){
            e.printStackTrace()
            Thread.currentThread().interrupt()
        }
        //Kode di atas akan dijalankan pada thread terpisah secara asynchronous.
        //      Jadi kita tak lagi perlu membuat background thread seperti pada service sebelumnya.
        //Terakhir, IntentService tak perlu mematikan dirinya sendiri.
        //      Service ini akan berhenti dengan sendirinya ketika sudah selesai menyelesaikan tugasnya.
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy()")
    }
}
