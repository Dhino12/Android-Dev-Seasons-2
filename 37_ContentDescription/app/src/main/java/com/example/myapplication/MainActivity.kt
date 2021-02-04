package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pakeCount = 3
        //Menambahkan parameter dinamis yang ada pada string.xml (%1$s ,%2$d, %3$s )
        val hallo = resources.getString(R.string.hello_world,"Chino_chan",pakeCount,"Rize_chan")
        tv_hello.text = hallo

        val songCount = 5
        //Menambahkan parameter dinamis yang ada pada string.xml (%1$s ,%2$s)
        val pluralText = resources.getQuantityString(R.plurals.numberOfSongsAvailable,songCount,songCount)
        tv_plural.text = pluralText

        tv_xliff.text = resources.getString(R.string.app_homeurl)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_settings){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
//        Baris intent di atas akan membawa Anda
//        pada halaman penentuan bahasa di perangkat Android. Bahasa yang digunakan oleh
//        aplikasi akan berubah ketika Anda mengubah pilihan bahasa
//        pada halaman penentuan tersebut.
        return super.onOptionsItemSelected(item)
    }
}