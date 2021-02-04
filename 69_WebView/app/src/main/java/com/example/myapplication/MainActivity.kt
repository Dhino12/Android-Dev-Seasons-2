package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Mengaktifkan Javascript pada WebView */
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Toast.makeText(this@MainActivity,"Web Berhasil di muat",Toast.LENGTH_SHORT).show()
                view?.loadUrl("javascript:alert('Web Berhasil dimuat')")
            }
        }
        //konfigurasi pada WebChromeClient.
        // webView.webChromeClient = WebChromeClient()
        /*
        * WebViewClient merupakan client yang ada pada WebView.
        * Anda bisa menerapkan tindakan seperti persiapan ketika webview dimuat,
        * atau ketika webview tersebut selesai memuat sebuah halaman.
        * Contoh pada kode di atas menampilkan sebuah pesan “Web Dicoding berhasil dimuat” melalui
        * JavaScript. Namun perlu Anda ketahui, untuk menampilkan sebuah alert dibutuhkan WebChromeClient.
        * */
        webView.webChromeClient = object : WebChromeClient(){
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsAlert(view, url, message, result)
                Toast.makeText(this@MainActivity,message,Toast.LENGTH_SHORT).show()
                result?.confirm()
                return true
            }
            /*
            * WebChromeClient merupakan client tambahan yang ada pada WebView dan mempunyai fungsi untuk menampilkan loading,
            * menampilkan alert atau perintah-perintah JavaScript lainnya.
            * Pada contoh di atas, kode tersebut akan menampilkan sebuah Toast dengan pesan sesuai dengan yang ada pada alert di JavaScript.
            * */
        }

        webView.loadUrl("https://www.dicoding.com")
        /*
        *  Kode di atas digunakan untuk memuat sebuah url dari website.
        *  Perlu Anda ketahui bahwa untuk OS Nougat ke atas, sebuah url harus menggunakan https.
        *  Jika Anda tidak menggunakan https atau hanya http saja,
        *  maka alamat tersebut tidak akan dimuat.
        * */
    }
}