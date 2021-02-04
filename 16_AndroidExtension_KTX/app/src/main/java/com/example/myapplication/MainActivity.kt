package com.example.myapplication

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.lv_list

//=====================================================
//Membuat List View Menggunakan ArrayAdapter
class MainActivity : AppCompatActivity() {
    //Memanggil adapter Hero
    private lateinit var adapter: HeroAdapater
    private lateinit var dataName:Array<String>
    private lateinit var dataDescription:Array<String>
    private lateinit var dataPhoto:TypedArray
    //Untuk Memasukan data data diatas
    private var heroes = arrayListOf<Hero>()

//    private val dataName = arrayOf(
//            "Cut Nyak Dien","Ki Hajar Dewantara","Moh Yamin","Patimura",
//            "R.A Kartini","Sukarno"
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val listView:ListView = findViewById(R.id.lv_list)
//        val arrAdapter = ArrayAdapter<String>(
//                this,android.R.layout.simple_list_item_1,android.R.id.text1,dataName
//        )
        //Mengirim context ke HeroAdapter
            //dan dibedakan context dan data
        adapter = HeroAdapater(this)
        //Android Extesion lv_list.adapter = adapter
        lv_list.adapter = adapter
        //untuk inisialisasi array
        prepare()
        //=======================
        addItem()
        lv_list.onItemClickListener = AdapterView.OnItemClickListener{
                _, _, position, _ ->
                Toast.makeText(this@MainActivity,heroes[position].name,Toast.LENGTH_SHORT).show()
        }
    }

    //inisialisai setiap data memnaggil array yang sudah dibuat di string.xml
    private fun prepare(){
        dataName = resources.getStringArray(R.array.data_name)
        dataDescription = resources.getStringArray(R.array.data_description)
        dataPhoto = resources.obtainTypedArray(R.array.data_photo)
    }

    //untuk Memasukan data data ke Arraylist supaya bisa diproses oleh adapter
    private fun addItem(){
        for (position in dataName.indices){
            val hero = Hero(
                    dataPhoto.getResourceId(position,-1),
                    dataName[position],
                    dataDescription[position]
            )
            //memasukan ke arraylist, lalu memanggil setter yang brada di adapter
                //dan memasukan arrayList heroes sebagai argumen
            heroes.add(hero)
        }

        //Memasukan data dari SetData ke ViewHolder
        adapter.pahlawan = heroes
    }
}