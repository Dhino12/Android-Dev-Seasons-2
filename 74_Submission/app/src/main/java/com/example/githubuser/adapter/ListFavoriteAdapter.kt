package com.example.githubuser.adapter

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.example.githubuser.model.FavoriteItems
import com.example.githubuser.view.CustomOnitemClickListener
import com.example.githubuser.view.ProfileActivity
import kotlinx.android.synthetic.main.item_users.view.*

class ListFavoriteAdapter(private val activity:Activity) : RecyclerView.Adapter<ListFavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<FavoriteItems>()
        set(listFavorite){
            if(listFavorite.size > 0){
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    fun addItem(favorite:FavoriteItems){
        this.listFavorite.add(favorite)
        notifyItemInserted(this.listFavorite.size - 1)
    }

    fun updateItem(position: Int, favorite:FavoriteItems){
        this.listFavorite[position] = favorite
        notifyItemChanged(position,favorite)
    }

    fun removeItem(position: Int){
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,this.listFavorite.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users,parent,false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFavorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite:FavoriteItems){
            with(itemView){
                item_userName.text      = favorite.userName
                item_repo.text          = favorite.repository
                item_followers.text     = favorite.follower
                item_following.text     = favorite.following
                title_Follower.text     = resources.getString(R.string.follower)
                title_Following.text    = resources.getString(R.string.following)

                if(favorite.profImage != null){
                    Glide.with(itemView.context)
                        .load(favorite.profImage)
                        .apply(RequestOptions())
                        .into(img_photos_user)
                }else{
                    Glide.with(itemView.context)
                        .load(favorite.avatarProf)
                        .apply(RequestOptions())
                        .into(img_photos_user)
                }

                cardItemUser.setOnClickListener(CustomOnitemClickListener(adapterPosition, object : CustomOnitemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, ProfileActivity::class.java)

                                TaskStackBuilder.create(activity)
                                    .addParentStack(ProfileActivity::class.java)
                                    .addNextIntent(intent)
                                    .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT)

                                intent.putExtra(ProfileActivity.EXTRA_POSISI, position)
                                intent.putExtra(ProfileActivity.EXTRA_FAVORITE, favorite)
                                activity.startActivityForResult(
                                    intent,
                                    ProfileActivity.REQUEST_UPDATE
                                )
                            }
                        })
                )
            }
        }
    }

}