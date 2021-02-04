package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() ,View.OnClickListener{

    private lateinit var mainViewModel: MainViewModel
    private lateinit var edtWidth:EditText
    private lateinit var edtHeight:EditText
    private lateinit var edtLength:EditText
    private lateinit var tvResult:TextView
    private lateinit var btnCalculateVolume:Button
    private lateinit var btnCalculateSurfaceArea:Button
    private lateinit var btnCalculateCirumReference:Button
    private lateinit var btnSave:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = MainViewModel(CuboidModel())
        edtHeight = findViewById(R.id.edt_height)
        edtLength = findViewById(R.id.edt_length)
        edtWidth = findViewById(R.id.edt_width)
        tvResult = findViewById(R.id.tv_result)
        btnCalculateCirumReference = findViewById(R.id.btn_calculate_cirumference)
        btnCalculateVolume = findViewById(R.id.btn_calculate_volume)
        btnCalculateSurfaceArea = findViewById(R.id.btn_calculate_surface_area)
        btnSave = findViewById(R.id.btn_save)

        btnSave.setOnClickListener(this)
        btnCalculateVolume.setOnClickListener(this)
        btnCalculateSurfaceArea.setOnClickListener(this)
        btnCalculateCirumReference.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val panjang = edtLength.text.toString().trim()
        val lebar = edtWidth.text.toString().trim()
        val tinggi = edtHeight.text.toString().trim()

        when{
            panjang.isEmpty() -> edtLength.error = "Field Ini Tidak Boleh Kosong"
            lebar.isEmpty() -> edtWidth.error = "Field Ini Tidak Boleh Kosong"
            tinggi.isEmpty() -> edtHeight.error = "Field Ini Tidak Boleh Kosong"
            else -> {
                val l = panjang.toDouble()
                val h = tinggi.toDouble()
                val w = lebar.toDouble()

                when (v.id) {
                    R.id.btn_save -> {
                        mainViewModel.save(l,w,h)
                        visible()
                    }
                    R.id.btn_calculate_surface_area -> {
                        tvResult.text = mainViewModel.getSurfaceArea().toString()
                        gone()
                    }
                    R.id.btn_calculate_cirumference -> {
                        tvResult.text = mainViewModel.getCirumference().toString()
                        gone()
                    }
                    R.id.btn_calculate_volume -> {
                        tvResult.text = mainViewModel.getVolume().toString()
                        gone()
                    }
                }
            }
        }
    }

    private fun visible(){
        btnCalculateVolume.visibility = View.VISIBLE
        btnCalculateCirumReference.visibility = View.VISIBLE
        btnCalculateSurfaceArea.visibility = View.VISIBLE
        btnSave.visibility = View.GONE
    }

    private fun gone(){
        btnCalculateVolume.visibility = View.GONE
        btnCalculateCirumReference.visibility = View.GONE
        btnCalculateSurfaceArea.visibility = View.GONE
        btnSave.visibility = View.VISIBLE
    }
}