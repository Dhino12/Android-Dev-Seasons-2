package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //mengubah actionnBar(Judul Halaman) di aplikasi
        //dengan support ActionBar = yang mendukung semua OS android
        //menggunakan ActionBar karena MainActivity inherit ke AppCompatActivity()
        if(supportActionBar != null){
            (supportActionBar as ActionBar).title = "Google Pixel"
        }
    }
}