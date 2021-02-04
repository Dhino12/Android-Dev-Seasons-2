package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sms_receiver.*

class SmsReceiverActivity : AppCompatActivity() , View.OnClickListener{

    companion object{
        const val EXTRA_SMS_NO = "extra_sms_no"
        const val EXTRA_SMS_MASSEGE = "extra_sms_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_receiver)

        title = getString(R.string.incoming_massage)
        btn_close.setOnClickListener(this)

        //Setelah obyek intent diterima, ReceiverActivity akan ditampilkan dengan background transparan.
        // Ini akan membuatnya seolah-olah menjadi sebuah dialog. Dengan cara seperti ini, pesan dapat ditampilkan.
        val senderNo = intent.getStringExtra(EXTRA_SMS_NO)
        val senderMessage = intent.getStringExtra(EXTRA_SMS_MASSEGE)

        tv_from.text = getString(R.string.from, senderNo)
        tv_massage.text = senderMessage
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.btn_close){
            finish()
        }
    }
}