package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_hero.view.*


//Kelas adapter yang akan memformat tiap element dari koleksi data yang akan ditampilkan
class ListHeroAdapter(private val listHero: ArrayList<Hero>) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {

    //Mendeklrasikan object yang akan menjadi onClick
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero,parent,false)
        return ListViewHolder(view)
    }

    // ========================= ViewHolder Pattern ====================================
    // ia akan memeriksa memori apakah item view yang hendak ditampilkan
    // tertentu sudah berada di memori  atau belum. Jika belum, maka akan dijalankan sebuah
    // proses yang cukup mahal dari segi memori, yaitu dijalankannya onCreateViewHolder()
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        //Akan Mengakses bind yang ada pada class ListViewHolder
        holder.bind(listHero[position])
    }
    //===================================================================================

    override fun getItemCount(): Int = listHero.size

    // ========================= ViewHolder Pattern ====================================
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hero: Hero){
            with(itemView){
                Glide.with(itemView.context)
                    .load(hero.photo)
                    .apply(RequestOptions().override(55,55))
                    .into(img_item_photo)
                tv_item_name.text = hero.name
                tv_item_description.text = hero.description
                //Menambah onClick =================================
                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(hero)
                }
            }
        }
    }
    //===================================================================================
    //Mengirim data ketika onClick berjalan
    interface OnItemClickCallback{
        fun onItemClicked(data: Hero)
    }
}