package com.example.consumerapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 class FavoriteItems(
   var id:Int? = null,
   var name: String? = null,
   var userName: String? = null,
   var repository: String? = null,
   var follower: String? = null,
   var following: String? = null,
   var company: String? = null,
   var location: String? = null,
   var avatarProf: String? = null,
   var profImage: Int?= null,
   var favImage: Int? = null,
   var favIcon: ByteArray? = null,
   var colors: Int = 0,
   var position_:Int = 0,
   var date: String? = null
):Parcelable