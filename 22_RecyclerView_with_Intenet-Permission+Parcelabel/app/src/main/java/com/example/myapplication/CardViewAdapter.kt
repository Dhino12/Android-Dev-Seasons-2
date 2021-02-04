package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_cardview_hero.view.*

class CardViewAdapter(private val listHero: ArrayList<Hero>) : RecyclerView.Adapter<CardViewAdapter.CardViewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview_hero,parent,false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listHero[position])
    }

    override fun getItemCount(): Int = listHero.size

    class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hero: Hero){
            with(itemView){
                Glide.with(itemView.context)
                    .load(hero.photo)
                    .apply(RequestOptions().override(350,550))
                    .into(img_item_photo_cardview)
                tv_item_name_cardView.text = hero.name
                tv_item_description_cardView.text = hero.description

                btn_favorite.setOnClickListener{
                    Toast.makeText(itemView.context,"Favorite ${hero.name}",Toast.LENGTH_SHORT).show()
                }
                btn_share.setOnClickListener{
                    Toast.makeText(itemView.context,"Share ${hero.name}",Toast.LENGTH_SHORT).show()
                }
                itemView.setOnClickListener{
                    Toast.makeText(itemView.context,"Kamu Memilih ${hero.name}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}