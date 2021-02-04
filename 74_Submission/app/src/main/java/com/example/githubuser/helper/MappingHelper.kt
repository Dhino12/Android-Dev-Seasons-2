package com.example.githubuser.helper

import android.database.Cursor
import com.example.githubuser.database.DatabaseContract
import com.example.githubuser.model.FavoriteItems

object MappingHelper {

    fun mapCursorToArrayList(notesCursor:Cursor?):ArrayList<FavoriteItems>{
        val favList = ArrayList<FavoriteItems>()

        notesCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val userName = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val follower = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWER))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPOSITORY))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val image = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.IMAGE))
                val favicon = getBlob(getColumnIndexOrThrow(DatabaseContract.UserColumns.FAVICON))
                favList.add(FavoriteItems(id,name,userName,repository,follower,following,company,location,image,favIcon = favicon ))
            }
        }
        return favList
    }

    fun mapCursorToObject(userCursor:Cursor?):FavoriteItems{
        var favorite = FavoriteItems()
        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
            val userName = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
            val follower = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWER))
            val following = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
            val repository = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPOSITORY))
            val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
            val image = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.IMAGE))
            val favicon = getBlob(getColumnIndexOrThrow(DatabaseContract.UserColumns.FAVICON))
            favorite = FavoriteItems(id,name,userName,repository,follower,following,company,location,image,favIcon = favicon )
        }
        return favorite
    }
}