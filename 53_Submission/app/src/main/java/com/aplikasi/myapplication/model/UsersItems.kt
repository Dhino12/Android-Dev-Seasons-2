package com.aplikasi.myapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 class UsersItems(
   var name: String,
   var userName: String,
   var repository: String,
   var follower: String,
   var following: String,
   var company: String,
   var location: String,
   var avatarProf: String,
   var profImage: Int?= null ,
   var colors: Int = 0,
   var position_:Int = 0,
   var visibility:Int
):Parcelable