package com.example.myapplication.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion._ID
import java.sql.SQLException

//sebuah kelas yang akan mengakomodasi kebutuhan DML
class NoteHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database:SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME

        //===============================================================
        //Kelas di bawah menggunakan sebuah pattern yang bernama Singleton Pattern.
        // Dengan singleton sebuah objek hanya bisa memiliki sebuah instance
        //digunakan untuk menginisiasi database
        private var INSTANCE: NoteHelper? = null
        fun getInstance(context: Context): NoteHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: NoteHelper(context)
            }
        //Synchronized di sini dipakai untuk menghindari duplikasi instance di semua Thread,
        //===============================================================
    }

    //===============================================================
    //metode untuk membuka dan menutup koneksi ke database-nya.
    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }
    fun close(){
        databaseHelper.close()

        if(database.isOpen)
            database.close()
    }
    //===============================================================

    //===============================================================
    //buat metode untuk melakukan proses CRUD-nya,
    // [ metode pertama adalah untuk mengambil data.]
    //===============================================================
    fun queryAll():Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }
    // [ metode pertama adalah untuk mengambil data.]
    fun queryById(id:String):Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }
    //===============================================================
    //===============================================================
    // [ metode kedua adalah untuk Menyimpan data.]
    //Proses penambahan data pada NoteHelper dijabarkan dalam bentuk seperti berikut dengan objek Notesebagai parameter inputnya:
    //===============================================================
    fun insert(values:ContentValues?):Long{
        return database.insert(DATABASE_TABLE,null,values)
    }
    //===============================================================
    //===============================================================
    // [ metode kedua adalah untuk Memperbaharui data.]
    //Sementara itu, pembaharuan data dijabarkan dalam bentuk berikut dengan objek Note terbaru (Catatan : _IDsebagai referensinya).
    //===============================================================
    fun update(id:String, values:ContentValues?):Int{
        return database.update(DATABASE_TABLE,values,"$_ID = ?", arrayOf(id))
    }
    //===============================================================
    //===============================================================
    // [ metode kedua adalah untuk Menghapus data.]
    // Lebih lanjut, proses penghapusan data pada NoteHelper dijabarkan dalam metode deleteById().
    // Id-nya berasal dari item Note yang dipilih sebagai acuan untuk menghapus data.
    //===============================================================
    fun deleteById(id:String):Int{
        return database.delete(DATABASE_TABLE,"$_ID = '$id'",null)
    }
    //===============================================================
    //============= PENTING =========================================
    //Benar, image tidak didukung oleh SQLite, maka untuk menyimpan image dalam SQLite kita harus mengkonversikan dulu kedalam byte.
    //===============================================================
}