package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , View.OnClickListener{

    //Kemudian MainActivity diregistrasikan untuk mendengar event/action dengan tag: ACTION_DOWNLOAD_STATUS.
    // Ketika event/action tersebut ditangkap oleh MainActivity, maka obyek downloadReceiver akan dijalankan.
    private lateinit var downloadReceiver: BroadcastReceiver
    companion object{
        private const val SMS_REQUEST_CODE = 101
        const val ACTION_DOWNLOAD_STATUS = "download_status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_permission.setOnClickListener(this)
        btn_download.setOnClickListener(this)

        //Menambahkan Intent untuk menjalankan DownloadService ==============
        downloadReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d(DownloadService.TAG,"Download Selesai")
                Toast.makeText(context,"Download Selesai",Toast.LENGTH_SHORT).show()
            }
        }

        val downloadIntentFilter = IntentFilter(ACTION_DOWNLOAD_STATUS)
        registerReceiver(downloadReceiver,downloadIntentFilter)
    }
    //====================================================================================

    //====================================================================================
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_permission -> {
                PermissionManager.check(this,android.Manifest.permission.RECEIVE_SMS,
                    SMS_REQUEST_CODE
                )
            }
            R.id.btn_download -> {
                val downloadServiceIntents=  Intent(this, DownloadService::class.java)
                startService(downloadServiceIntents)
            }
        }
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(requestCode == SMS_REQUEST_CODE){
            when{
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> Toast.makeText(this,"SMS Receiver permission diterima",Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this,"SMS Receiver permission ditolak",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Melakukan penghancuran objek receiver yang tlh diregistrasikan sblmny
        unregisterReceiver(downloadReceiver)
    }
}