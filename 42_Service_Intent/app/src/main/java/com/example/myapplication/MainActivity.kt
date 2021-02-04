package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener{

    //==================================================================================
    //membuat ServiceConnection untuk menghubungkan MainActivity dengan MyBoundService
    private var mServiceBound = false
    private lateinit var mBoundService: MyBoundService

    private val mServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MyBoundService.MyBinder
            mBoundService = myBinder.getService
            mServiceBound = true
        }
//Kode di atas merupakan sebuah listener untuk menerima callback dari ServiceConnetion. Kalau dilihat ada dua callback,
// yakni ketika mulai terhubung dengan kelas service dan juga ketika kelas service sudah terputus.
        override fun onServiceDisconnected(name: ComponentName?) {
            mServiceBound = false
        }
    }
    //==================================================================================

    //==================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start_service.setOnClickListener(this)
        btn_start_intent_service.setOnClickListener(this)
        btn_start_bound_service.setOnClickListener(this)
        btn_stop_bound_service.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_start_service -> {
                //Menjalankan Service
                //Setelah dijalankan, metode onStartCommand() pada MyService akan dijalankan.
                val mStartServiceIntent = Intent(this@MainActivity, MyService::class.java)
                startService(mStartServiceIntent)
            }
            R.id.btn_start_intent_service -> {
                //MyIntentService dijalankan. Service tersebut akan melakukan pemrosesan
                // obyek Intent yang dikirimkan dan menjalankan suatu proses yang berjalan di background.
                val mStartIntentService = Intent(this, MyIntentService::class.java)
                mStartIntentService.putExtra(MyIntentService.EXTRA_DURATION,5000L)
                startService(mStartIntentService)
            }
            R.id.btn_start_bound_service -> {
                //Menjalankan service
                val mBoundServiceIntent = Intent(this, MyBoundService::class.java)
                bindService(mBoundServiceIntent,mServiceConnection,Context.BIND_AUTO_CREATE)
                //Pada kode di atas kita menggunakan bindService yang digunakan untuk memulai mengikat kelas MyBoundService ke kelas MainActivity.
                // Sedangkan mBoundServiceIntent adalah sebuah intent eksplisit yang digunakan untuk menjalankan komponen dari dalam sebuah aplikasi.
                // Sedangkan mServiceConnection adalah sebuah ServiceConnection berfungsi sebagai callback dari kelas MyBoundService.
                // Kemudian ada juga BIND_AUTO_CREATE yang membuat sebuah service jika service tersebut belum aktif.
            }
            R.id.btn_stop_bound_service -> {
//                Selanjutnya untuk mengakhiri MyBoundService ubind
                unbindService(mServiceConnection)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mServiceBound){
            unbindService(mServiceConnection)
        }
    }
}