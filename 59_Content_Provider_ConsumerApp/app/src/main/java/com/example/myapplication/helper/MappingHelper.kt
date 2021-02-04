package com.example.myapplication.helper

import android.database.Cursor
import com.example.myapplication.db.DatabaseContract
import com.example.myapplication.entity.Note

/*
*  Setelah ini kita akan mengambil data dari method queryAll() yang ada di NoteHelper.
*  Namun, karena nanti di adapter kita akan menggunakan arraylist, sedangkan di sini objek yang di kembalikan berupa Cursor,
*  maka dari itu kita harus mengonversi dari Cursor ke Arraylist. Kita akan membuat kelas pembantu untuk menangani hal ini.
* */
object MappingHelper {

    fun mapCursorToArrayList(notesCursor:Cursor?):ArrayList<Note>{
        val notesList = ArrayList<Note>()

        notesCursor?.apply {
            //MoveToFirst di sini digunakan untuk memindah cursor ke baris pertama sedangkan
            // MoveToNext digunakan untuk memindahkan cursor ke baris selanjutnya.
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))
                notesList.add(Note(id,title,description,date))
            }
        }
        return notesList
    }

    //YANG DIUBAH (UNTUK CONTENT PROVIDER ) ======================================
    //fungsi tambahan pada kelas MappingHelper untuk konversi dari cursor menjadi object dengan
    // nama mapCursorToObject yang akan digunakan di kelas NoteAddUpdateActivity nanti.
    fun mapCursorToObject(notesCursor: Cursor?): Note {
        var note = Note()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
            val title = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
            val description = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
            val date = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))
            note = Note(id,title,description,date)
        }
        return note
    }
    //=======================================================================
}
/*
* Catatan:
* Fungsi apply digunakan untuk menyederhanakan kode yang berulang.
* Misalnya notesCursor.geInt cukup ditulis getInt dan
*  notesCursor.getColumnIndexOrThrow cukup ditulis getColumnIndexOrThrow.
* */