package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.text.DecimalFormat

class MyWorker(context: Context, workerParms: WorkerParameters) : Worker(context,workerParms) {

    private var resultStatus : Result? = null
    companion object{
        private val TAG =  MyWorker::class.java.simpleName
        const val APP_ID = "26b2837bae0ec67952d17b5ed5f94f56"
        const val EXTRA_CITY = "Jakarta"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "The Banannas Channel"
    }

    //Metode doWork adalah metode yang akan dipanggil ketika WorkManager berjalan.
    //. Ada beberapa status yang bisa dikembalikan yaitu:
        //Result.success(), result yang menandakan berhasil.
        //Result.failure(), result yang menandakan gagal.
        //Result.retry(), result yang menandakan untuk mengulang task lagi.
    override fun doWork():Result {
        //Fungsi di atas digunakan untuk membuat one-time request. Saat membuat request,
        // Anda bisa menambahkan data untuk dikirimkan dengan membuat object Data yang berisi data key-value,
            // key yang dipakai di sini yaitu MyWorker.EXTRA_CITY.
        // Setelah itu dikirimkan melalui setInputData. Kemudian untuk mendapatkan datanya di kelas Worker,

        val dataCity = inputData.getString(EXTRA_CITY)
        val result = getCurrentWeather(dataCity)

//        Anda membuat method getCurrentWeather yang mengembalikan nilai Result.
//        Dengan begitu, kita bisa menentukan kembalian dari proses ini apakah berhasil atau gagal dengan menggunakan kode tersebut.
        return result
    }

    private fun getCurrentWeather(city:String?): Result{
        Log.d(TAG,"getCurrentWeather: Mulai $city.......")
        val client = SyncHttpClient()
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$APP_ID"
        Log.d(TAG,"getCurrentWeather: $url")
        client.post(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val result = responseBody?.let { String(it) }
                Log.d(TAG,result.toString())

                try {
                    val responseObject = JSONObject(result)
                    val currentWeather :String = responseObject.getJSONArray("weather").getJSONObject(0).getString("main")
                    val description: String = responseObject.getJSONArray("weather").getJSONObject(0).getString("description")
                    val tempInKelvin = responseObject.getJSONObject("main").getDouble("temp")
                    val tempInCelsius = tempInKelvin - 273
                    val temprature:String = DecimalFormat("##.##").format(tempInCelsius)
                    val title = "Current Weather In $city"
                    val message = "$currentWeather, $description with $temprature Celsius"
                    showNotification(title,message)
                    resultStatus = Result.success()
                }catch (e:Exception){
                    showNotification("Get Current Weather Not Success", e.message)
                    Log.d(TAG,"onSuccess: Gagal........")
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d(TAG,"onFailure: Gagal..........")
                // ketika proses gagal, maka jobFinished diset dengan parameter true. Yang artinya job perlu di reschedule
                showNotification("Get Current Weather Failed",error?.message)
                resultStatus = Result.failure()
            }
        })
        return  resultStatus as Result
    }

    private fun showNotification(title:String,description:String?){
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification:NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_baseline_announcement_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID,notification.build())
    }

}