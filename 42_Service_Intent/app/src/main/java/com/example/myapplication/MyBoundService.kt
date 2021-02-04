package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class MyBoundService : Service() {

    companion object{
        private val TAG = MyBoundService::class.java.simpleName
    }
    private val mBinder = MyBinder()
    private val startTime = System.currentTimeMillis()
    //===============================================================================================
    internal inner class MyBinder : Binder(){
        val getService: MyBoundService = this@MyBoundService
    }
    //Kode di atas adalah kelas yang dipanggil di metode onServiceConnected untuk memanggil kelas service. Fungsinya untuk mengikat kelas service
    //===============================================================================================

    //===============================================================================================
    //Metode onCreate() dipanggil ketika memulai pembentukan kelas MyBoundService.
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate: ")
    }
    //===============================================================

    //===============================================================================================
    //ervice akan berjalan dan diikatkan atau ditempelkan dengan activity pemanggil.
    // Pada metode ini juga, mTimer akan mulai berjalan.
    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG,"onBind: ")
        mTimer.start()
        return mBinder
    }
    //===============================================================

    //===============================================================
    //onDestroy() yang ada di MyBoundService ini berfungsi untuk melakukan penghapusan kelas MyBoundService dari memori.
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy: ")
    }
    //===============================================================
//Kode onDestroy() seperti yang dijelaskan di metode sebelumnya, akan memanggil unBindService atau melakukan pelepasan service dari Activity.
// Pemanggilan unbindService di dalam onDestroy ditujukan untuk mencegah memory leaks dari bound services.
//============================================================================================

    //===============================================================
//  unbindService(mServiceConnection) ketika kode disamping akan melepaskan service dari activity
    //dan memanggil Secara tidak langsung maka ia akan memanggil metode unBind
    //setelah metode onUnBind dipanggil, maka ia akan memanggil metode onDestroy()
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG,"onUbind: ")
        mTimer.cancel()
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG,"onRebind: ")
    }
    //================================ Count Down (Hitung Mundur ====================================
    private var mTimer : CountDownTimer = object : CountDownTimer(100000,1000){
        override fun onTick(millisUntilFinished: Long) {
//            Hitungan mundur tersebut berfungsi untuk melihat proses terikatnya kelas MyBoundService ke MainActivity.
            val elapseTime = System.currentTimeMillis() - startTime
            Log.d(TAG,"onTick: $elapseTime")
        }

        override fun onFinish() {

        }
    }
    //==============================================================================================
}
