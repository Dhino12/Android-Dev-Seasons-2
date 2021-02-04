package com.example.githubuser.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuser.database.DatabaseContract.UserColumns
import com.example.githubuser.database.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_NAME = "dbnoteapp"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_USER =
            "CREATE TABLE $TABLE_NAME" +
                    "(${UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${UserColumns.USERNAME} TEXT," +
                    "${UserColumns.NAME} TEXT ," +
                    "${UserColumns.IMAGE} TEXT ," +
                    "${UserColumns.FAVICON} BLOB ," +
                    "${UserColumns.LOCATION} TEXT ," +
                    "${UserColumns.COMPANY} TEXT ," +
                    "${UserColumns.REPOSITORY} TEXT ," +
                    "${UserColumns.FOLLOWING} TEXT ," +
                    "${UserColumns.FOLLOWER} TEXT )"
    }


//    fun insertData(
//        name: String?,
//        price: String?,
//        image: ByteArray?
//    ) {
//        val database = writableDatabase
//        val sql = "INSERT INTO FOOD VALUES (NULL, ?, ?, ?)"
//        val statement = database.compileStatement(sql)
//        statement.clearBindings()
//        statement.bindString(1, name)
//        statement.bindString(2, price)
//        statement.bindBlob(3, image)
//        statement.executeInsert()
//    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}