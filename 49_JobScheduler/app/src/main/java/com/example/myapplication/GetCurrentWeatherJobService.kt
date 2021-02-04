package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.text.DecimalFormat

class GetCurrentWeatherJobService : JobService(){

    companion object{
        private val TAG = GetCurrentWeatherJobService::class.java.simpleName

        //Isikan dengan nama kota
        internal const val CITY = "Jakarta"

        //Isikan dengan API Key
        internal const val APP_ID = "26b2837bae0ec67952d17b5ed5f94f56"
    }

    //Metode onStartJob adalah metode yang akan dipanggil ketika scheduler berjalan.
    //Hal ini dilakukan untuk mengindikasikan bahwa metode tersebut telah menjalankan sebuah
    // proses pada thread yang berbeda. Proses yang dimaksud adalah proses mengambil data dari Internet.
    //Lalu pada situasi apa nilai kembaliannya false? Ketika proses di dalam onStartJob hanya sebentar.
    // Ini mengindikasikan bahwa proses di dalam metode tersebut tidak berjalan di thread yang berbeda.
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG,"onStartJob()")
        getCurrentWeather(params)
        return true
    }

    //Sedangkan metode onStopJob akan dipanggil ketika job sedang berjalan akan tetapi belum selesai dikarenakan kondisi nya tidak terpenuhi.
    override fun onStopJob(params: JobParameters?): Boolean {
        //Metode onStopJob akan dijalankan jika job sudah berjalan, akan tetapi kondisinya kemudian tidak terpenuhi.
        Log.d(TAG,"onStopJob()")
        return true
    }

    private fun getCurrentWeather(job: JobParameters?){
        Log.d(TAG,"getCurrentWeather: Mulai.....")
        val client = AsyncHttpClient()
        val url = "http://api.openweathermap.org/data/2.5/weather?q=$CITY&appid=$APP_ID"
        Log.d(TAG,"getCurrentWeather: $url")
        client.get(url, object : AsyncHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }
                Log.d(TAG,result.toString())
                try {
                    //==============================================================================
                    //Melakukan koneksi HTTP ke openweathermap.org. Data akan diakses menggunakan
                    // endpoint berikut : http://api.openweathermap.org/data/2.5/weather?q="+CITY+"&appid="+APP_ID
                    //Pada contoh ini kita akan meminta data cuaca untuk kota Jakarta.
                    //==============================================================================
                    val responseObject = JSONObject(result)
                    val currentWeather = responseObject.getJSONArray("weather").getJSONObject(0).getString("main")
                    val description = responseObject.getJSONArray("weather").getJSONObject(0).getString("description")

                    val tempInKelvin = responseObject.getJSONObject("main").getDouble("temp")
                    val tempInCelsius = tempInKelvin - 273
                    val temprature = DecimalFormat("##.##").format(tempInCelsius)
                    //Kode di atas digunakan untuk memformat tampilan agar hanya ada dua nilai dibelakang koma.

                    val title = "Current Weather"
                    val message = "$currentWeather, $description with $temprature celsius"
                    val notifID = 100

                    showNotification(applicationContext, title, message, notifID)

                    Log.d(TAG,"onSuccess: Selesai.....")

                    //=============================================================================
                    //Ketika proses berjalan di thread yang berbeda, maka proses tersebut dapat mengabarkan kapan dia telah selesai.
                    // Caranya dengan menjalankan jobFinished()
                    jobFinished(job,false)
                    //=============================================================================

                }catch (e:Exception){
                    Log.d(TAG,"onSuccess: Gagal....")
                    //=============================================================================
//                    Ketika jobFinished dijalankan, ini mengindikasikan bahwa proses yang berjalan tidak mengalami masalah dengan cara
//                    memasang nilai false pada parameter keduanya
                    //Sangat penting bagi job untuk mejalankan jobFinished ketika ia sudah selesai.
                    // Bila job tidak dinyatakan selesai, perangkat Android tidak dapat masuk ke state idle kembali.
                    // Konsekuensi lainnya job ini akan memakan daya baterai yang lebih banyak.
                    jobFinished(job,true)
                    //=============================================================================
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d(TAG,"onFailur: Gagal.......")
                jobFinished(job,true)
            }
        })
    }

    //Menampilkan Notification di panel notofikasi pada prangkat pengguna
    private fun showNotification(context: Context,title:String ,message:String, notifID: Int){
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "Job schaduler channel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.baseline_replay_black_18dp)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setSound(alarmSound)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifID,notification)
    }

}