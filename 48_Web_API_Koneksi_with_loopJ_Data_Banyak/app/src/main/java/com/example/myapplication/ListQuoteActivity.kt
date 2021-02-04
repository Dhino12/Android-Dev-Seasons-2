package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_list_quote.*
import org.json.JSONArray
import java.lang.Exception

class ListQuoteActivity : AppCompatActivity() {

    companion object{
        private val TAG = ListQuoteActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_quote)

        supportActionBar?.title = "List of Quotes"
        getListQuote()
    }

    private fun getListQuote(){
        progressBarList.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://programming-quotes-api.herokuapp.com/quotes/page/1"
        client.get(url, object : AsyncHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                //Jika Koneksi Berhasil
                progressBarList.visibility = View.INVISIBLE
                //Di sini kita lihat terdapat JSONArray yang berisi banyak JSON Object.
                // Maka Anda perlu melakukan perulangan untuk mendapatkan data tersebut dengan cara seperti ini:
                //Parse JSON
                val listQuote = ArrayList<String>()
                val result = responseBody?.let { String(it) }
                if (result != null) {
                    Log.d(TAG, result)
                }

                try {
                    //====================================================================
                    //Karena dimulai dengan [ ], artinya ia bertipe JSONArray. Karena itu, untuk mengambil datanya
                    val jsonArray = JSONArray(result)
                    //====================================================================
                    //Selanjutnya kita menggunakan perulangan untuk mendapatkan JSONObject yang ada di dalamnya
                    for(i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val quote = jsonObject.getString("en")
                        val author = jsonObject.getString("author")
                        listQuote.add("\n$quote\n - $author\n")
                    }
                    //====================================================================
                    //Setelah mendapatkan data dalam bentuk ArrayList, maka Anda dapat memasukkannya ke adapter ListView seperti biasa
                    val adapter = ArrayAdapter(this@ListQuoteActivity,android.R.layout.simple_list_item_1,listQuote)
                    listQuotes.adapter = adapter
                    //==================================================================== 
                }catch (e:Exception){
                    Toast.makeText(this@ListQuoteActivity,e.message,Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                //Jika Koneksi Gagal
                progressBarList.visibility = View.INVISIBLE

                //================================================================
                //Di sini Anda menggunakan percabangan untuk mengganti pesan yang ditampilkan jika terjadi eror saat koneksi ke server
                val errorMessage = when(statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                    // Lalu jika selain kode di atas Anda dapat menggunakan error.getMessage() untuk mendapatkan pesan eror.
                }
                //================================================================
                Toast.makeText(this@ListQuoteActivity,errorMessage,Toast.LENGTH_SHORT).show()
            }
        })
    }
}