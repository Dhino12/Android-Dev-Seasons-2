package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import java.lang.Exception

class SmsReceiver : BroadcastReceiver() {

    private val TAG = SmsReceiver::class.java.simpleName


//    Di metode onReceive()receiver akan memproses metadata dari SMS yang masuk.
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        try {
            if(bundle != null){
                val pdusObj = bundle.get("pdus") as Array<Any>

                for (aPdusObj in pdusObj){

                    //==============================================================================
                    //Untuk memperoleh obyek dari kelas SmsMessage, yaitu obyek currentMessage
                    //kita menggunakan metode getIncomingMessage().
                    // Metode ini akan mengembalikan currentMessage berdasarkan OS yang dijalankan oleh perangkat Android.
                    // Hal ini perlu dilakukan karena metode SmsMessage.createFromPdu((object);
                    // sudah deprecated(usang) di peranti dengan OS Marshmallow atau versi setelahnya.
                    val currentMessage = getIncomeingMessage(aPdusObj ,bundle)
                    //==============================================================================

                    val senderNum = currentMessage.displayOriginatingAddress
                    val message = currentMessage.displayMessageBody

                    Log.d(TAG,"senderNum : $senderNum; message : $message")

                    //==============================================================================
                    //Selanjutnya ReceiverActivity akan dijalankan dgn membawa data melalui sbuah intent
                    //    showSmsIntent
                    val showSmsIntent = Intent(context, SmsReceiverActivity::class.java)

                    //===============================================================================
                    //pada flag dbwh akan menjalankan Activity pada task yang berbeda.
                    // Bila Activity tersebut sudah ada di dalam stack, maka ia akan ditampilkan ke layar
                    showSmsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    //===============================================================================

                    showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_NO,senderNum)
                    showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_MASSEGE,message)
                    context.startActivity(showSmsIntent)
                }

            }
        } catch (e:Exception){
            Log.d(TAG,"Exception smsReceiver $e")
        }

    }

    private fun getIncomeingMessage(aObject:Any,bundles:Bundle): SmsMessage{
        val currentSMS : SmsMessage
        if(Build.VERSION.SDK_INT >= 23 ){
            val format = bundles.getString("format")
            currentSMS = SmsMessage.createFromPdu(aObject as ByteArray,format)
        } else currentSMS = SmsMessage.createFromPdu(aObject as ByteArray)
        return currentSMS
    }
}
