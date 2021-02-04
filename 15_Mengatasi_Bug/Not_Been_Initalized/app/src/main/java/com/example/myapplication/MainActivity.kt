package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.StringBuilder

// ! KODE INI AKAN MENGHASILKAN !
//Caused by: kotlin.UninitializedPropertyAccessException: lateinit property btnSetValue has not been initialized
class MainActivity : AppCompatActivity() ,View.OnClickListener{
    private lateinit var btnSetValue:Button
    private lateinit var tvText:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvText = findViewById(R.id.tv_text)
        //Solusinya
//        btnSetValue = findViewById(R.id.btn_setValue)
        //===========================================
        btnSetValue.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.btn_setValue){
            tvText.text = "19"
        }
    }
}