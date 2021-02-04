package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapater: WeatherAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapater = WeatherAdapter()
        adapater.notifyDataSetChanged()

        recylcerView.layoutManager = LinearLayoutManager(this)
        recylcerView.adapter = adapater

        mainViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        btnCity.setOnClickListener{
            val city = editCity.text.toString()
            if(city.isEmpty()) return@setOnClickListener
            showLoading(true)
            mainViewModel.setWeather(city)
        }

        mainViewModel.getWeather().observe(this, Observer { weatherItems ->
            if(weatherItems != null){
                adapater.setData(weatherItems)
                showLoading(false)
            }
        })

    }

    private fun showLoading(state:Boolean){
        if(state){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }



}