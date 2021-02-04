package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    //Menjalankan Proses Async =========
    companion object {
        private const val INPUT_STRING = "Halo ini DEMO AsyncTask!!"
        private const val LOG_ASYNC = "Demo Async"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_status.setText(R.string.status_pre)
        tv_desc.text = INPUT_STRING

        GlobalScope.launch (Dispatchers.IO){
            //Background Test
            val input = INPUT_STRING
            var output: String? = null

            Log.d(LOG_ASYNC,"status: doInBackground")

            try {
                //Input stringnya ditambahkan dengn string Selamat Belajar!!
                output = "[ $input ] Selamat Belajar!!"
                delay(2000)

                //Pindah ke mainThread untuk update UI
//                Di sini kita menggunakan withContext(Dispatchers.Main) karena kita
//                perlu pindah ke Main Thread untuk update UI berupa TextView,
//                jika tidak menggunakan ini, maka UI/TextView tidak akan pernah ter-update.
                withContext(Dispatchers.Main){
                    Log.d(LOG_ASYNC,"status: doPostExecute")
                    tv_status.setText(R.string.status_post)
                    tv_desc.text = output
                }
            } catch (e: Exception){
                Log.d(LOG_ASYNC,e.message.toString())
            }
        }
    }
}