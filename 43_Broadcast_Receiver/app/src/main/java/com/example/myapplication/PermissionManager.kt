package com.example.myapplication

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionManager {
    // Permission untuk android Marshmallow
    fun check (activity:Activity, permission:String ,requestCode:Int){
        if(ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, arrayOf(permission),requestCode)
        }
    }

}