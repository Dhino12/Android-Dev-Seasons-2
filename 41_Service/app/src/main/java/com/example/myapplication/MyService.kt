package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException

class MyService : Service() {

    //Kekurangan dari service tipe ini adalah ia tak menyediakan
    // background thread diluar ui thread secara default.
    // Jadi tiada cara lainnya selain membuat thread secara sendiri.
    companion object{
        internal val TAG = MyService::class.java.simpleName
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not Yet Implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand")
        GlobalScope.launch {
            delay(3000)
            //sopSelf = berfungsi untuk memberhentikan/mematikan MyService dari sistem Android
            stopSelf()
            Log.d(TAG,"Service dihentikan")
        }
        //START_STICKY menandakan bahwa service tsb dimatikan olh sistem android krna kekurangan memori
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy: ")
    }
}
