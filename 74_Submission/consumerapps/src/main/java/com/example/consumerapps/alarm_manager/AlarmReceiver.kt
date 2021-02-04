package com.example.consumerapps.alarm_manager

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.consumerapps.R
import com.example.consumerapps.view.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        const val TYPE_ONE_TIME = "OneTimeAlarm"
        const val TYPE_REPEATING = "RepeatingAlarm"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        private const val TIME_FORMAT = "HH:mm"
        private const val ID_ONETIME = 100
        private const val ID_REPEATING = 101
    }



    private fun isDateInvalid(date:String,format:String):Boolean{
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient =false
            df.parse(date)
            Log.d("TestAlarm","Alarm Validasi")
            false
        }catch (e:ParseException){
            Log.d("TestAlarm","Alarm Catch: $format")
            true
        }
    }

    fun setRepeatingAlarm(context: Context , type:String, time:String, message:String){
        if(isDateInvalid(time,TIME_FORMAT)) return

        val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentRepeating = Intent(context,AlarmReceiver::class.java)
        intentRepeating.putExtra(EXTRA_MESSAGE, message)

        val putExtra = intentRepeating.putExtra(EXTRA_TYPE,type)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND,0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING,intentRepeating,0)
        alarmManger.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        isAlarmSet(context, type)
        Toast.makeText(context,"Repeating Alarm Setup:$context, $time, $message",Toast.LENGTH_LONG).show()
    }

    override fun onReceive(context: Context?,intent: Intent?) {
        val type = intent?.getStringExtra(EXTRA_TYPE)
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        val title = if(type.equals(TYPE_ONE_TIME,ignoreCase = true)) TYPE_ONE_TIME else TYPE_REPEATING
        val notifID = if(type.equals(TYPE_ONE_TIME,ignoreCase = true)) ID_ONETIME else ID_REPEATING
        Toast.makeText(context,"Sudah Waktunya $message",Toast.LENGTH_LONG).show()
        Log.d("error","masuk Receive")
        showAlarmNotification(context,title,message,notifID)
    }

    private fun showAlarmNotification(context: Context?,title:String,message:String?,notifId:Int){

        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManger chanel"

        val notifSetting = Intent(context,MainActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(context)
            .addNextIntent(notifSetting)
            .getPendingIntent(110,PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManagerCompat = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.yuu)
            .setContentTitle(title)
            .setContentText("Ketuk untuk Kembali ke GithubUser Apps")
            .setColor(ContextCompat.getColor(context,android.R.color.transparent))
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId,notification)
    }

    fun cancelAlarm(context: Context?,type:String){
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,AlarmReceiver::class.java)
        val requestCode = if(type.equals(TYPE_ONE_TIME,ignoreCase = true)) ID_ONETIME else ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0)

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context,"Repeating Alarm dibatalkan",Toast.LENGTH_LONG).show()
    }
    private fun isAlarmSet(context: Context?, type:String):Boolean{
        val intent = Intent(context,AlarmReceiver::class.java)
        val requestCode = if(type.equals(TYPE_ONE_TIME,ignoreCase = true)) ID_ONETIME else ID_REPEATING

        return PendingIntent.getBroadcast(context,requestCode,intent,PendingIntent.FLAG_NO_CREATE) != null
    }
}