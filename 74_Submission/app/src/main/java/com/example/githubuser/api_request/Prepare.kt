package com.example.githubuser.api_request

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import com.example.githubuser.R
import com.example.githubuser.adapter.ListUsersAdapter
import com.example.githubuser.model.UsersItems

class Prepare(private val mContext:Context) {

    fun prepare(adapter: ListUsersAdapter):ArrayList<UsersItems>{
        val listItem = ArrayList<UsersItems>()

        val dataColors: TypedArray = mContext.resources.obtainTypedArray(R.array.flat)
        val dataPhoto: TypedArray = mContext.resources.obtainTypedArray(R.array.avatar)
        val dataName:Array<String> = mContext.resources.getStringArray(R.array.name)
        val dataUserName:Array<String> = mContext.resources.getStringArray(R.array.username)
        val dataFollower:Array<String> = mContext.resources.getStringArray(R.array.followers)
        val dataRepo:Array<String> = mContext.resources.getStringArray(R.array.repository)
        val dataFollowing:Array<String> = mContext.resources.getStringArray(R.array.following)
        val dataLocation:Array<String> = mContext.resources.getStringArray(R.array.location)
        val dataCompany:Array<String> = mContext.resources.getStringArray(R.array.company)

        for(position in dataUserName.indices){
            val users = UsersItems(
                dataName[position],
                dataUserName[position],
                dataRepo[position],
                dataFollower[position],
                dataFollowing[position],
                dataCompany[position],
                dataLocation[position],
                "",
                profImage = dataPhoto.getResourceId(position,-1),
                colors = dataColors.getResourceId(position,-1),
                position_ = position,
                visibility = View.VISIBLE
            )
            listItem.add(users)
            adapter.setData(listItem)
        }
        return listItem
    }
}