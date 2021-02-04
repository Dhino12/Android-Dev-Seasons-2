package com.example.myapplication.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.myapplication.db.DatabaseContract.AUTHORITY
import com.example.myapplication.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.example.myapplication.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.example.myapplication.db.NoteHelper

class NoteProvider : ContentProvider() {
    // deklarasikan variabel terlebih dahulu dan definisikan UriMatcher untuk mengecek URI yang masuk,
    // apakah bersifat all atau ada tambahan id-nya.
    companion object{
        /*
        * UriMatcher, fungsinya adalah untuk membandingkan uri dengan nilai integer tertentu.
        * Sebelum dibandingkan, kita perlu atur nilai tiap Uri-nya terlebih dahulu.
        * Perhatikan nilai dari NOTE dan NOTE_ID yaitu 1 dan 2. Kemudian kita set nilai dari Uri yang akan kita gunakan.
        *   Contoh :
        * Uri pertama : content://com.dicoding.mynotesapp/note akan kita cocokkan dengan nilai int 1.
        * Uri kedua : content://com.dicoding.mynotesapp/note/id akan kita cocokan dengan nilai int 2.
        * */
        /*
        Integer digunakan sebagai identifier antara select all sama select by id
         */
        private const val NOTE =  1
        private const val NOTE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var noteHelper: NoteHelper

        /*
                Uri matcher untuk mempermudah identifier dengan menggunakan integer
                misal
                uri com.dicoding.picodiploma.mynotesapp dicocokan dengan integer 1
                uri com.dicoding.picodiploma.mynotesapp/# dicocokan dengan integer 2
                 */
        init {
            // content://com.example.mynotesapp/note
            sUriMatcher.addURI(AUTHORITY,TABLE_NAME, NOTE)
            // content://com.example.mynotesapp/note/id
            sUriMatcher.addURI(AUTHORITY,"$TABLE_NAME/#", NOTE_ID)
            // Ini menandakan bahwa tanda # nanti akan diganti dengan id tertentu,
            // sama seperti fungsi ? di dalam query atau %s di dalam string.
        }
    }

    override fun onCreate(): Boolean {
        noteHelper = NoteHelper.getInstance(context as Context)
        noteHelper.open()
        return true
    }

    /*
        Method queryAll digunakan ketika ingin menjalankan queryAll Select
        Return cursor
         */
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor:Cursor?
        //Ketika obyek Uri yang digunakan cocok dengan nilai pada variabel NOTE atau int 1,
        // maka query yang akan dijalankan adalah select semua data yang ada di dalam database.
        when(sUriMatcher.match(uri)){
            NOTE -> cursor = noteHelper.queryAll()
            NOTE_ID -> cursor = noteHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        //Dan ketika obyek Uri yang digunakan cocok dengan nilai pada variable NOTE_ID atau int 2, maka query yang akan dijalankan adalah select satu data berdasarkan id.
        // Nah, kita bisa mengambil id-nya dari obyek Uri dengan menggunakan fungsi getLastPathSegment() yang artinya ambil segment terakhir dari obyek Uri.
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added:Long = when(NOTE){
            sUriMatcher.match(uri) -> noteHelper.insert(values)
            else -> 0
        }

        // Kemudian setelah selesai, kita akan memberitahu kalau ada perubahan data dengan memanggil metode:
        //Fungsi ini akan mengirim pesan kepada semua aplikasi yang mengakses data dari content provider ini.
        context?.contentResolver?.notifyChange(CONTENT_URI,null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val update:Int = when(NOTE_ID){
            sUriMatcher.match(uri) -> noteHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI,null)

        return update
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when(NOTE_ID){
            sUriMatcher.match(uri) -> noteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI,null)

        return deleted
    }
}
