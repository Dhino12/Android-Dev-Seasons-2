package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.myapplication.services.DataManagerService
import com.example.myapplication.services.DataManagerService.Companion.CANCEL_MESSAGE
import com.example.myapplication.services.DataManagerService.Companion.FAILED_MESSAGE
import com.example.myapplication.services.DataManagerService.Companion.PREPARATION_MESSAGE
import com.example.myapplication.services.DataManagerService.Companion.SUCCESS_MESSAGE
import com.example.myapplication.services.DataManagerService.Companion.UPDATE_MESSAGE
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

//Selesai sudah materi preload data dengan menggunakan mekanisme transactional. Dengan menggunakan mekanisme ini,
// pemrosesan data dalam jumlah banyak akan berjalan dengan cepat.
class MainActivity : AppCompatActivity(), HandlerCallback {

    private lateinit var mBoundService:Messenger
    private var mServiceBound:Boolean = false

    /*
     Service Connection adalah interface yang digunakan untuk menghubungkan antara boundservice dengan activity
      */
    private val mServiceConnection = object: ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBoundService = Messenger(service)
            mServiceBound = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ikat dan lepaskan kelas DataManagerService ke MainActivity.
        val mBoundServiceIntent = Intent(this@MainActivity, DataManagerService::class.java)
        val mActivityMessenger = Messenger(IncomingHandler(this))
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER,mActivityMessenger)

        bindService(mBoundServiceIntent,mServiceConnection,Context.BIND_AUTO_CREATE)

    }

    override fun onDestroy() {
        super.onDestroy()

        unbindService(mServiceConnection)
    }

    override fun onPreparation(){
        Toast.makeText(this,"MEMULAI MEMUAT DATA",Toast.LENGTH_LONG).show()
    }

    override fun updateProgress(progress: Long) {
        Log.d("PROGRESS","updateProgress: $progress")
        progress_bar.progress = progress.toInt()
    }

    override fun loadSuccess() {
        Toast.makeText(this,"Berhasil",Toast.LENGTH_LONG).show()
        startActivity(Intent(this@MainActivity, MahasiswaActivity::class.java))
        finish()
    }

    override fun loadFailed() {
        Toast.makeText(this,"Gagal",Toast.LENGTH_LONG).show()
    }

    override fun loadCancle() {
        finish()
    }

    // kelas Handler di MainActivity untuk menerima data dari service yang nantinya akan dikirimkan ke HandlerCallback.
    // Lalu bagaimana menerima data dari kelas Service? Dengan menggunakan Handler Anda bisa mengolah data yang dikirimkan oleh Messager dari kelas Service.
    /*
    * Ketika kondisi dari handleMessage sesuai, maka WeakReference akan
    * mengirim nilai balikan sesuai dengan kondisi yang diterima melalui HandlerCallback
    * dan di dalam kelas MainActivity Anda bisa memberi aksi pada kondisi tersebut.
    * Seperti ketika berhasil memuat data, maka akan berpindah ke kelas MahasiswaActivity.
    * */
    private class IncomingHandler(callback: HandlerCallback):Handler(){
        private var weakCallback: WeakReference<HandlerCallback> = WeakReference(callback)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                PREPARATION_MESSAGE -> weakCallback.get()?.onPreparation()
                UPDATE_MESSAGE -> {
                    val bundle = msg.data
                    val progress = bundle.getLong("KEY_PROGRESS")
                    weakCallback.get()?.updateProgress(progress)
                }
                SUCCESS_MESSAGE -> weakCallback.get()?.loadSuccess()
                FAILED_MESSAGE -> weakCallback.get()?.loadFailed()
                CANCEL_MESSAGE -> weakCallback.get()?.loadCancle()
            }
        }
    }
}

private interface HandlerCallback {
    fun onPreparation()

    fun updateProgress(progress:Long)

    fun loadSuccess()

    fun loadFailed()

    fun loadCancle()
}
