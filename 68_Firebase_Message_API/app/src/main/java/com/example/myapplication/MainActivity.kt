package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        * metode untuk subscribe topic dan menampilkan refresh token ketika tombol ditekan.
        * */
        btn_subscribe.setOnClickListener{
            FirebaseMessaging.getInstance().subscribeToTopic("news")
            // Kode di atas berfungsi untuk melakukan subscribe atau berlangganan topic “news.”
            val msg = getString(R.string.msg_subscribed)
            Log.d(TAG,msg)
            Toast.makeText(this@MainActivity,msg,Toast.LENGTH_SHORT).show()
        }
        btn_token.setOnClickListener {
            // fungsi untuk mendapatkan refresh token.
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                instanceIdResult ->
                val deviceToken = instanceIdResult.token
                val msg = getString(R.string.msg_token_fmt,deviceToken)
                Toast.makeText(this@MainActivity,msg,Toast.LENGTH_SHORT).show()
                Log.d(TAG,"Refreshed token: $deviceToken")
            }
        }
    }
}