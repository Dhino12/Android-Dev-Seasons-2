package com.aplikasi.mysubmmision

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
    val username:String,
    val reopsitory:String,
    val follower:String,
    val following:String,
    val image:Int,
    val name: String,
    val location:String,
    val company:String,
    val colors:Int
):Parcelable