package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private val dataName = arrayOf(
            "Cut Nyak Dien","Ki Hajar Dewantara","Moh Yamin"
            ,"Patimura","R.A Kartini","Sukarno"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.lv_list)
        val arrAdapter = ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,android.R.id.text1,dataName
        )
        listView.adapter = arrAdapter
    }
}