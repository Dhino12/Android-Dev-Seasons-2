package com.example.myapplication

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        const val TYPE_ONE_TIME = "OneTimeAlarm"
        const val TYPE_REPEATING = "RepeatingAlarm"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        //Di sini kita menggunakan dua konstanta bertipe data integer untuk menentukan notif ID sebagai ID untuk menampilkan notifikasi kepada pengguna.

        private const val ID_ONETIME = 100
        private const val ID_REPEATING = 101
//        Dua baris di atas adalah konstanta untuk menentukan tipe alarm. Dan selanjutnya, dua baris di bawah ini adalah konstanta untuk intent key.
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val title = if(type.equals(TYPE_ONE_TIME, ignoreCase = true)) TYPE_ONE_TIME else TYPE_REPEATING
        val notifID = if(type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONETIME else ID_REPEATING

        showToast(context,title,message)
        //Menambahkan notif
        showAlarmNotification(context, title, message, notifID)
    }

    private fun showToast(context:Context,title: String,message:String?){
        Toast.makeText(context,"$title : $message",Toast.LENGTH_LONG).show()
    }

    //==========================================================================================
    //=========================== UNTUK MENGATUR ALARM ========================================
    //==========================================================================================
    fun setOneTimeAlarm(context: Context, type:String, date:String, time:String, message:String){

        val DATE_FORMAT = "yyyy-MM-dd"
        val TIME_FORMAT = "HH:mm"
        // Validasi inputan date dan time terlebih dahulu
        if(isDateInvalid(date,DATE_FORMAT) || isDateInvalid(time,  TIME_FORMAT)) {
            Log.d("testAlarm","Masuk kedalm IF")
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentAlaram = Intent(context, AlarmReceiver::class.java)
        intentAlaram.putExtra(EXTRA_MESSAGE,message)
        intentAlaram.putExtra(EXTRA_TYPE,type)
        //Pada kode di atas kita membuat sebuah obyek dari kelas AlarmManager.
        // Kita menyiapkan sebuah Intent yang akan menjalankan AlarmReceiver dan membawa data alarm dan pesan.
        //

        Log.e("ONE_TIME","$date $time")
        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split(":").toTypedArray()

        val calender = Calendar.getInstance()
        calender[Calendar.YEAR] = dateArray[0].toInt()
        calender[Calendar.MONTH] = dateArray[1].toInt() - 1
        calender[Calendar.DAY_OF_MONTH] = dateArray[2].toInt()
        calender[Calendar.HOUR_OF_DAY] = timeArray[0].toInt()
        calender[Calendar.MINUTE] = timeArray[1].toInt()
        calender[Calendar.SECOND] = 0
        //Pada kode di atas kita memecah data date dan time untuk mengambil nilai tahun, bulan, hari, jam dan menit.
        //Masalahnya adalah, nilai bulan ke 9 pada kelas Calendar bukanlah bulan September. Ini karena indeksnya dimulai dari 0. Jadi,
        // untuk memperoleh bulan September, maka nilai 9 tadi harus kita kurangi 1.
        //================ SAMA SAJA ATAU ==================================
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
//        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
//        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
//        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
//        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
//        calendar.set(Calendar.SECOND, 0)

        //Intent yang dibuat akan dieksekusi ketika waktu alarm sama dengan waktu pada sistem Android.
        // Di sini komponen PendingIntent  akan diberikan kepada BroadcastReceiver.
        val pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME,intentAlaram,0)
        alarmManager.set(AlarmManager.RTC_WAKEUP,calender.timeInMillis,pendingIntent)

        Log.d("testAlarm","Alarm berhasil di setup")
        Toast.makeText(context,"One Time Alarm Set up",Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String,format:String):Boolean{
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            Log.d("testAlarm","Alarm Validasi")
            false
        } catch (e:ParseException){
            Log.d("testAlarm","Alarm catch : $format")
            true
        }
    }
    //==========================================================================================

    //==========================================================================================
    //================ UNTUK MELIHAT NOTIFIKASI HASIL DARI ALARM MANAGER ======================
    //==========================================================================================
    private fun showAlarmNotification(context: Context,title:String,message:String?, notifID:Int){
        val CHANNEL_ID = "Channel_1"
        val CHANNER_NAME = "AlarmManager channel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_access_alarms_black_18dp)
            .setContentTitle(title)
            .setContentText(message.toString())
            .setColor(ContextCompat.getColor(context,android.R.color.transparent))
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel =  NotificationChannel(CHANNEL_ID,CHANNER_NAME,NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifID,notification)
    }
    //Metode di atas merupakan sebuah metode untuk membuat dan menampilkan notifikasi yang kompatibel dengan beragam API dari Android.
    // Metode ini memanfaatkan fasilitas NotificationCompat. Anda akan lebih jauh memahami tentang Notifikasi ini pada modul berikutnya.
    // Untuk bagian OS Oreo ke atas, harus menggunakan NotificationChannel untuk menampilkan Notifikasi.
    //==========================================================================================

}
