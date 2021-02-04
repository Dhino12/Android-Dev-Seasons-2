package com.example.myapplication

import android.content.Context

class UserPreference(context: Context) {
    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val AGE = "age"
        private const val PHONE_NUMBER = "phone"
        private const val LOVE_MU = "islove"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)

    // Setter dan Getter
    fun setUser(value: UserModel){
        //Untuk menyimpan data kita harus mengakses obyek editor yang dimiliki oleh preferences.
        val editor = preferences.edit()
        editor.putString(NAME,value.name)
        editor.putString(EMAIL,value.email)
        editor.putInt(AGE,value.age)
        editor.putString(PHONE_NUMBER,value.phoneNumber)
        //=============================================================================================================
        //Jika Anda lihat pada kode setter di atas maka ada pemanggilan metode editor.apply(),
        // di sinilah data akan disimpan ke dalam preferences. Selain apply() ada metode lainnya yaitu commit().
        // Perbedaan dari Apply dan Commit adalah pada mekanismenya. Apply dijalankan secara asynchronous, sedangkan Commit secara synchronous.
        editor.apply()
        //=============================================================================================================
    }

    fun getUser(): UserModel {
        //Sedangkan getName di sini berguna untuk mengambil value yang ada di preference lalu menerapkan di class model.
        val model = UserModel()
        model.name = preferences.getString(NAME,"")
        model.email = preferences.getString(EMAIL,"")
        model.age = preferences.getInt(AGE,0)
        model.phoneNumber = preferences.getString(PHONE_NUMBER,"")
        model.isLove = preferences.getBoolean(LOVE_MU,false)
        return model
    }

}