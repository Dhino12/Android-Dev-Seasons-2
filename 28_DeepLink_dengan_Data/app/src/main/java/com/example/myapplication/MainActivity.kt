package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navCTRL = this.findNavController(R.id.container)
        NavigationUI.setupActionBarWithNavController(this,navCTRL)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navCTRL = this.findNavController(R.id.container)
        return navCTRL.navigateUp()
    }
}