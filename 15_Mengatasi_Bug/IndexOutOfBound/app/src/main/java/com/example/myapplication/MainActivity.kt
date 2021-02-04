package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.StringBuilder

// ! KODE INI AKAN MENGHASILKAN !
//Caused by: kotlin.UninitializedPropertyAccessException: lateinit property btnSetValue has not been initialized
class MainActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var btnSetValue: Button
    private lateinit var tvText: TextView
    private var names = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvText = findViewById(R.id.tv_text)
        btnSetValue = findViewById(R.id.btn_setValue)
        btnSetValue.setOnClickListener(this)

        names.add("Latina")
        names.add("Hatena")
        names.add("Chino")
    }

    override fun onClick(v: View) {
        if(v.id == R.id.btn_setValue){
            val name = StringBuilder()
            // ! java.lang.IndexOutOfBoundsException: Index: 3, Size: 3 ! ERROR
            //karena data ada 3 sedangkan index dimulai dari 0
            //jadi melebihi batas data, sehrusnya sampai 2 untuk index looping
            for(i in 0..3){
                name.append(names[i]).append("\n")
            }
            //Solusi ==========================
//            for(i in 0..2){
//                name.append(names[i]).append("\n")
//            }
            tvText.text = name.toString()
        }
    }
}