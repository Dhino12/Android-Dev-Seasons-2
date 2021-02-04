package com.aplikasi.mysubmmision


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

class ListUserAdapter internal constructor(private var listUser: ArrayList<Person>): RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallBack:OnItemClickCallback

    fun setItemClickCallback(onItemClickCallback:OnItemClickCallback){
        this.onItemClickCallBack = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_users,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        Glide.with(holder.itemView.context)
            .load(user.image)
            .apply(RequestOptions())
            .into(holder.imgPhoto)
        holder.tvName.text = user.username
        holder.tvRepository.text = user.reopsitory
        holder.tvFollower.text = user.follower
        holder.tvFollowing.text = user.following
        holder.itemView.setOnClickListener {
            onItemClickCallBack.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data:Person)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvName:TextView = itemView.findViewById(R.id.item_userName)
        var tvRepository:TextView = itemView.findViewById(R.id.item_rep)
        var tvFollower:TextView = itemView.findViewById(R.id.item_followers)
        var tvFollowing:TextView = itemView.findViewById(R.id.item_following)
        var imgPhoto:CircleImageView = itemView.findViewById(R.id.img_photos_user)
    }

}