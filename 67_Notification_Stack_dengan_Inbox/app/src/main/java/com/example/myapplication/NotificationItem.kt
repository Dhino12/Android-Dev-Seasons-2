package com.example.myapplication

data class NotificationItem (
    var id: Int,
    var sender: String?,
    var message: String?
)
//Kelas ini berfungsi sebagai holder untuk notifikasi yg akan diguna n
// Ia akan berisikan nama pengirim dan isi pesan dari sebuah notifikasi.