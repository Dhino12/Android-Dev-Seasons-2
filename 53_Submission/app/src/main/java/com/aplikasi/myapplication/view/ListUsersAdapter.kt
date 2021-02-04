package com.aplikasi.myapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.myapplication.R
import com.aplikasi.myapplication.model.UsersItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_users.view.*

class ListUsersAdapter  : RecyclerView.Adapter<ListUsersAdapter.ListUserViewHolder>() {

    private val mData = ArrayList<UsersItems>()
    private lateinit var onItemClickCallBack: OnItemClickCallback

    fun setData(items: ArrayList<UsersItems>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun setItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallBack = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: UsersItems)
    }

    class ListUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userItems: UsersItems){
            with(itemView){
                item_userName.text      = userItems.userName
                item_repo.text          = userItems.repository
                item_followers.text     = userItems.follower
                item_following.text     = userItems.following
                title_Follower.text     = resources.getString(R.string.follower)
                title_Following.text    = resources.getString(R.string.following)

                item_followers.visibility   = userItems.visibility
                item_following.visibility   = userItems.visibility
                item_repo.visibility        = userItems.visibility

                title_Follower.visibility   = userItems.visibility
                title_Following.visibility  = userItems.visibility
                title_repo.visibility       = userItems.visibility

                if(userItems.profImage != null){
                    Glide.with(itemView.context)
                        .load(userItems.profImage)
                        .apply(RequestOptions())
                        .into(img_photos_user)
                }else{
                    Glide.with(itemView.context)
                        .load(userItems.avatarProf)
                        .apply(RequestOptions())
                        .into(img_photos_user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_users,parent,false)
        return ListUserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        holder.bind(mData[position])
        holder.itemView.setOnClickListener {
            onItemClickCallBack.onItemClicked(mData[holder.adapterPosition])
        }
    }

}