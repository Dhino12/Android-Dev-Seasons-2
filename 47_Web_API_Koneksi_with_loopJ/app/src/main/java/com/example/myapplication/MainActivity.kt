package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRandomQuote()
    }

    private fun getRandomQuote(){
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://programming-quotes-api.herokuapp.com/quotes/random"
        client.get(url,object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                //Jika Koneksi Berhasil
                //Berikut ini adalah cara untuk mem-parsing data tersebut di dalam method onSuccess:
                progressBar.visibility = View.INVISIBLE

                val result = responseBody?.let { String(it) }
                    Log.d(TAG,result.toString())
                try {
                    val responseObject = JSONObject(result!!)
                    val quote = responseObject.getString("en")
                    val author = responseObject.getString("author")
                    tvQuote.text = quote
                    tvAuthor.text = author

                    //val jsonObject = JSONObject(response);
                    //Parse Array dalam JSON [] ==============================
                    //val dataArray = jsonObject.getJSONArray("data")
                    //Parse Object dalam Array di JSON {} ==============================
                    //val dataObject = dataArray.getJSONObject(0)
                    //Parse data dalam Object JSON "" ==============================
                    //val email = dataObject.getString("email")
                    //=================== Cara Singkatnya ================================
//                    val jsonObject = JSONObject(response);
//                    val dataArray = jsonObject.getJSONArray("data")
//                    val email = dataArray.getJSONObject(0).getString("email")
                    //=================================================================
                }catch (e:Exception){
                    Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
                //Data kemudian ditampilkan di TextView jika berhasil. Jika tidak, akan muncul Toast dan memberitahukan eror apa yang terjadi.
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                //Jika Koneksi Gagal
            }
            //Di sini terdapat ProgressBar yang ditampilkan untuk mengindikasikan proses loading data. Loading ini akan hilang ketika data sudah ditampilkan.
        })
    }
}