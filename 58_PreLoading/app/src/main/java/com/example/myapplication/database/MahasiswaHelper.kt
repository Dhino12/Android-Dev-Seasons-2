package com.example.myapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.example.myapplication.database.DatabaseContract.MahasiswaColumns.Companion.NAMA
import com.example.myapplication.database.DatabaseContract.MahasiswaColumns.Companion.NIM
import com.example.myapplication.database.DatabaseContract.TABLE_NAME
import com.example.myapplication.model.MahasiswaModel

class MahasiswaHelper(context: Context) {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database:SQLiteDatabase

    //Kemudian kita akan membuat konstruktor dan melakukan inisilasisi dari DatabaseHelper yang sudah dibuat sebelumnya
    companion object{
        private var INSTANCE: MahasiswaHelper? = null

        fun getInstance(context: Context): MahasiswaHelper {
            if(INSTANCE == null){
                synchronized(SQLiteOpenHelper::class.java){
                    if(INSTANCE == null){
                        INSTANCE = MahasiswaHelper(context)
                    }
                }
            }
            return INSTANCE as MahasiswaHelper
        }
    }

    // membuat untuk membuka dan menutup koneksi dari database yang sudah dibuat.
    @Throws(SQLiteException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    //menambahkan perintah terhadap database yang sudah dibuat sebelumnya seperti membaca dan menulis ke database mahasiswa
    fun getAllData():ArrayList<MahasiswaModel>{
        val cursor = database.query(TABLE_NAME,null,null,null,null,null,"$_ID ASC",null)
        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel
        if(cursor.count > 0){
            do {
                mahasiswaModel = MahasiswaModel()
                mahasiswaModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                mahasiswaModel.name = cursor.getString(cursor.getColumnIndexOrThrow(NAMA))
                mahasiswaModel.nim = cursor.getString(cursor.getColumnIndexOrThrow(NIM))

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            }while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insert(mahasiswaModel: MahasiswaModel):Long{
        val initialValues = ContentValues()
        initialValues.put(NAMA,mahasiswaModel.name)
        initialValues.put(NIM,mahasiswaModel.nim)
        return database.insert(TABLE_NAME,null,initialValues)
    }

    //Memulai transaction
    fun beginTransaction(){
        database.beginTransaction()
    }

    //===================================================================================================
    // Kita perlu memastikan untuk menjalankan setTransactionSuccesful() ketika proses transactional berhasil diselesaikan.
    // Bila Anda ingin membatalkan proses transactional tersebut, misalnya karena eror,  maka kita tak perlu menjalankan setTransactionSuccessful().
    fun setTransactionSuccess(){
        database.setTransactionSuccessful()
    }
    //===================================================================================================

    //===================================================================================================
    fun endTransaction(){
        database.endTransaction()
    }
//    Kode di atas menunjukkan bahwa transaction selesai.
//===================================================================================================

//    Kemudian dengan menggunakan looping, satu per satu data dimasukkan ke dalam database dengan menggunakan metode insertTransaction().
    fun insertTransaction(mahasiswaModel: MahasiswaModel){
        val sql = ("INSERT INTO $TABLE_NAME ($NAMA, $NIM) VALUES (?, ?)")
        val stmt = database.compileStatement(sql)
        stmt.bindString(1,mahasiswaModel.name)
        stmt.bindString(2, mahasiswaModel.nim)
        stmt.execute()
        stmt.clearBindings()
    }
    //=============================================================================================
    //untuk melakukan pencarian berdasarkan nama. Ini akan berguna ketika Anda membutuhkannya.
    fun getDataByName(nama:String):ArrayList<MahasiswaModel>{
        val cursor = database.query(TABLE_NAME,null,"$NAMA LIKE ?", arrayOf(nama),null,null,"$_ID ASC",null)
        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel
        if (cursor.count > 0){
            do {
                mahasiswaModel = MahasiswaModel()
                mahasiswaModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                mahasiswaModel.name = cursor.getString(cursor.getColumnIndexOrThrow(NAMA))
                mahasiswaModel.nim = cursor.getString(cursor.getColumnIndexOrThrow(NIM))

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            }while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }
    //=============================================================================================

}