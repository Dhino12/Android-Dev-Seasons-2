package com.example.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() , MyAsyncCallback {

    //Menjalankan Proses Async =========
    companion object{
        private const val INPUT_STRING = "Halo ini DEMO AsyncTask!!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Menjalankan Proses Async =========
        val demoAsync = DemoAsync(this)
        demoAsync.execute(INPUT_STRING)
    }

    //================================ Melakukan Proses Async =================================
//    AsyncTask ini memiliki 3 tipe generik.
//
//    Param : Menerima inputan obyek string.
//    Progress : Void ⇒ Merupakan obyek dari void dengan kata lain, tidak ada progress yang akan ditampilkan.
//    Result : Hasil dari proses berupa obyek string.
    private class DemoAsync(myListener: MyAsyncCallback) : AsyncTask<String, Void, String>(){
        companion object{
            private val LOG_ASYNC = "DemoAsync"
        }

        //. WeakReference disarankan untuk menghindari memory leak(kebocoran memori) yang bisa terjadi dalam AsyncTask
        //ini bisa terjadi ketika aplikasi sudah ditutup, akan tetapi proses asynctask masih tetap berjalan.
        private val myListeners : WeakReference<MyAsyncCallback> = WeakReference(myListener)

//==========================================================================================
//        menambahkan metode-metode di dalam kelas DemoAsync
//        untuk persiapan dan ketika proses sudah berhasil.

        override fun onPreExecute() {

//      Metode ini akan dijalankan pertama kali sebelum proses asynchronous dilakukan. Metode ini masih berada pada ui thread.

            super.onPreExecute()
            Log.d(LOG_ASYNC,"status : onPreExecute")

            //Kelas DemoAsync menggunakan WeakReference untuk mengisi status yang ada
            // pada bagian callback tersebut. WeakReference dipanggil di bagian berikut
            val myListenerd = myListeners.get()
            myListenerd?.onPreExecute()
//            Informasi tersebut akan dikirim ke kelas MainActivity melalui myListener.onPreExecute()

        }

        override fun doInBackground(vararg params: String?): String {

//            Metode ini akan dijalankan setelah onPreExecute(). Di sinilah proses asynchronous terjadi.

            Log.d(LOG_ASYNC,"status : doInBackground")
            var output: String? = null

            try {
                val input = params[0]
                output = "[ $input ] Selamat Belajar"
                Thread.sleep(2000)
            } catch (e: Exception){
                Log.d(LOG_ASYNC,"${e.message}")
            }
            return output.toString()
        }

        override fun onPostExecute(result: String) {

//            Setelah proses di doInBackground() selesai, maka hasilnya akan dikirimkan ke metode onPostExecute().
//            ketika proses doInBackground telah selesai dan akan dijalankan di main thread yang mana state/kondisi ini dapat mengakses view.
            //terakhir akan mnampilkan hasil proses yg dilakukan doInBackground
            //Nilai dari string result akan mnjadi "Halo ini Demo AsyncTask Selamat Belajar!!"

            super.onPostExecute(result)
            Log.d(LOG_ASYNC,"status : onPostExecute")

            //WeakReference jga dipanggil di bagian berikut
            val myListenerd = this.myListeners.get()
            myListenerd?.onPostExecute(result)
            //Kode di atas akan mengirimkan informasi bahwa kelas DemoAsync sedang dalam proses onPostExecute atau proses sudah selesai.
        }
        //==========================================================================================
    }
    //============================= Akhir dari Async ===============================================

    override fun onPreExecute() {
        //Memberikan aksi ketika proses berlangsung
        tv_status.setText(R.string.status_pre)
        tv_desc.text = INPUT_STRING
        //    Kode di atas berfungsi untuk mempersiapkan asynctask. Di sini masih berjalan di main thread dan bisa digunakan untuk mengakses view.
    }

    override fun onPostExecute(text: String) {
        tv_status.setText(R.string.status_post)
        tv_desc.text = text
    }
}

internal interface MyAsyncCallback{
    fun onPreExecute()
    fun onPostExecute(text:String)
}