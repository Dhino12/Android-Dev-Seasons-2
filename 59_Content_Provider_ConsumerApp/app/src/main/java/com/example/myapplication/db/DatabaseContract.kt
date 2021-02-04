package com.example.myapplication.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    // ========================== Content URI.  ================
    const val AUTHORITY = "com.example.mynotesapp"
    const val SCHEME = "content"

    internal class NoteColumns:BaseColumns{
        companion object{
            const val TABLE_NAME = "note"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"

            //============= Untuk Membuat URI Content ===============
            //com.dicoding.picodiploma.mynotesapp/note
            // Variabel AUTHORITY merupakan base authority yang akan kita gunakan
            // untuk mengidentifikasi bahwa provider NoteProvider milik MyNotesApp yang akan diakses.
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
            //Di sini kita menggabungkan base authority dengan scheme dan nama tabel,
            // nanti string yang akan tercipta adalah "content://com.dicoding.mynotesapp/note".
            //Artinya dari string "content://com.dicoding.mynotesapp/note" berarti kita akan
            // mencoba untuk akses data tabel Note dari provider NoteProvider.
        }
    }
}