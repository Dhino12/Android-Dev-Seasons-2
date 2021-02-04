package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_grid_hero.view.*

class GridHeroAdapter(private val listHero: ArrayList<Hero>) : RecyclerView.Adapter<GridHeroAdapter.GridViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback =  onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_grid_hero,viewGroup,false)
        return GridViewHolder(view)
    }

    // ========================= ViewHolder Pattern ====================================
    // ia akan memeriksa memori apakah item view yang hendak ditampilkan
    // tertentu sudah berada di memori  atau belum. Jika belum, maka akan dijalankan sebuah
    // proses yang cukup mahal dari segi memori, yaitu dijalankannya onCreateViewHolder()
    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(listHero[position])
    }
    //===================================================================================

    override fun getItemCount(): Int = listHero.size

    // ========================= ViewHolder Pattern ====================================
    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hero: Hero){
            with(itemView){
                Glide.with(itemView.context)
                    .load(hero.photo)
                    .apply(RequestOptions().override(350,350))
                    .into(img_item_photo_grid)
                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(hero)
                }
            }
        }
    }
    //===================================================================================
    interface OnItemClickCallback{
        fun onItemClicked(data: Hero)
    }
}