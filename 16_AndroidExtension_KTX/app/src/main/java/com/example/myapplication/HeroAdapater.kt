package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.Hero

//Mengirim data atau melakukan suatu proses ketika objek diinisialisasi
    //constractor digunakan untuk menerima context dan membedakan menerima context dan mngirim data
class HeroAdapater internal constructor(private val context: Context):BaseAdapter(){
    //Set Data ===============================
        //menampung data yang dikirim dari aktivity dan digunakan sebagai sumber data
        //untuk dimasukan ke ViewHolder
    internal var pahlawan = arrayListOf<Hero>()

    private inner class ViewHolder internal constructor(private val view: View){
            private val txtName:TextView = view.findViewById(R.id.txt_name)
            private val txtDescription:TextView = view.findViewById(R.id.txt_description)
            private val imgPhoto:ImageView = view.findViewById(R.id.img_photo)

            internal fun bind(hero: Hero){
                //LayoutContainer
                with(view){
                    txtName.text = hero.name
                    txtDescription.text = hero.description
                    imgPhoto.setImageResource(hero.Photo)
                }
            }
    }

    //===================================================================================
    //Memanggil layout item_hero xml dan melakukan proses manipulasi setiap komponennya
        //Seperti textView dan imageView
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        //Proses pemanggilan textView dan setText
        var itemView = view
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_hero,parent,false)
        }

        val viewHolder = ViewHolder(itemView as View)
        val hero = getItem(position) as Hero
        viewHolder.bind(hero)
        return itemView
    }
    //===================================================================================

    override fun getItem(i: Int): Any {
        return pahlawan[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }


    //Method yang digunakan untuk mengatahui brp banyak item yang akan ditampilkan
    override fun getCount(): Int {
        return pahlawan.size
    }
    //===================================================================================

}