package com.example.consumerapps.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.consumerapps.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager.beginTransaction().add(R.id.settingHolder, MyPreferenceFragment()).commit()
    }
}