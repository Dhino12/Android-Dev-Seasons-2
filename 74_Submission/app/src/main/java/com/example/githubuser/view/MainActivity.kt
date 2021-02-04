package com.example.githubuser.view


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.githubuser.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Consumer Apps"
        drawerLayout = drawer_Layout
        val navCTRL = this.findNavController(R.id.nav_hostFragment)
        NavigationUI.setupActionBarWithNavController(this,navCTRL,drawerLayout)
        NavigationUI.setupWithNavController(navView,navCTRL)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navCtrl = findNavController(R.id.nav_hostFragment)

        return NavigationUI.navigateUp(navCtrl,drawerLayout) || super.onSupportNavigateUp()
    }
}