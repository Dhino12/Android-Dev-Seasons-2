package com.example.myapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mynotesapp.db.DatabaseContract.NoteColumns
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

//Tanggung jawab utama dari kelas di atas adalah menciptakan database dengan tabel yang dibutuhkan
// dan handle ketika terjadi perubahan skema pada tabel (terjadi pada metode onUpgrade()).
//Nah, di kelas ini kita menggunakan variabel yang ada pada DatabaseContract untuk mengisi kolom nama tabel. Begitu juga dengan kelas-kelas lainnya nanti.
// Dengan memanfaatkan kelas contract, maka akses nama tabel dan nama kolom tabel menjadi lebih mudah.
internal class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){
    companion object{

        private const val DATABASE_NAME = "dbnoteapp"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                "(${NoteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "${NoteColumns.TITLE} TEXT NOT NULL," +
                "${NoteColumns.DESCRIPTION} TEXT NOT NULL," +
                "${NoteColumns.DATE} TEXT NOTE NULL)"
    }
    override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_TABLE_NOTE)
    }


    /*
    Method onUpgrade akan di panggil ketika terjadi perbedaan versi
    Gunakan method onUpgrade untuk melakukan proses migrasi data
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        /*
        Drop table tidak dianjurkan ketika proses migrasi terjadi dikarenakan data user akan hilang,
        */
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}