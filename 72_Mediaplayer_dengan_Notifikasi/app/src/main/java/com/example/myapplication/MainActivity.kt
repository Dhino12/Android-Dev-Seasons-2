package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // untuk memanggil kelas MediaService.
    private val TAG = MainActivity::class.java.simpleName
    private var mService:Messenger? = null
    private lateinit var mBoundServiceIntent: Intent
    private var mServiceBound = false

    private val mServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName, service: IBinder?) {
            mService = Messenger(service)
            mServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_play.setOnClickListener(this)
        btn_stop.setOnClickListener(this)

        /*
        * Fungsi startService digunakan untuk membuat dan menghancurkan kelas service,
        * sedangkan bindService berfungsi untuk mengaitkan kelas service ke kelas MainActivity.
        * */
        mBoundServiceIntent = Intent(this, MediaService::class.java)
        mBoundServiceIntent.action = MediaService.ACTION_CREATE
        /*
        * Dengan memasang nilai MediaService pada action,
        * kita dapat menentukan command mana yang ingin kita jalankan.
        * Ketika startService() dipanggil maka pada saat itu onStartCommand() berjalan.
        * */

        startService(mBoundServiceIntent)
        bindService(mBoundServiceIntent, mServiceConnection , Context.BIND_AUTO_CREATE)
    }

    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.btn_play -> {
                //Pada kode di atas, menggunakan bantuan Messenger untuk mengirim perintah PLAY dan STOP.
                //Lihat penerima pesan atau IncomingHandler di bagian MediaService
                if(!mServiceBound) return
                try {
                    mService?.send(Message.obtain(null, MediaService.PLAY,0,0))
                }catch (e:RemoteException){
                    e.printStackTrace()
                }
            }
            R.id.btn_stop -> {
                if(!mServiceBound) return
                try {
                    mService?.send(Message.obtain(null, MediaService.STOP,0,0))
                }catch (e:RemoteException){
                    e.printStackTrace()
                }
            }
            else -> {}
        }
    }

    // kode berikut untuk mengakhiri MediaService ketika kelas MainActivity dalam keadaan mati.
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy:...")
        Toast.makeText(this,"Masuk onDestroy",Toast.LENGTH_SHORT).show()
        unbindService(mServiceConnection) //unbindService untuk melepaskan ikatan service dari activity.
        mBoundServiceIntent.action = MediaService.ACTION_DESTROY

        startService(mBoundServiceIntent)

        /*
        * Pada method onDestroy(),
        * kita perlu melepaskan memori MediaPlayer ketika sudah tidak digunakan.
        * Ini penting dilakukan untuk menghindari masalah IllegalStateException.
        * */
    }


}