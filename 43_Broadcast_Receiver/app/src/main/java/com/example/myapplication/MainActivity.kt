package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , View.OnClickListener{

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
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_permission -> {
                PermissionManager.check(this,android.Manifest.permission.RECEIVE_SMS,
                    SMS_REQUEST_CODE
                )
            }
            R.id.btn_download -> {
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

}