package com.example.myapplication

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.example.myapplication.db.NoteHelper
import com.example.myapplication.entity.Note
import com.example.myapplication.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//============ Menerapkan Content Provider =============================
//Pertama, ubah penggunaan model data Note menjadi cursor.
//Kedua, ubah akses database dengan menggunakan ContentResolver.
//Ketiga, hapus penggunaan komponen yang sudah tidak perlukan, misalnya NoteHelper.
//=====================================================================

//Tugas utama MainActivity ada dua. Pertama, menampilkan data dari database pada tabel Note secara ascending.
// Kedua, menerima nilai balik dari setiap aksi dan proses yang dilakukan di NoteAddUpdateActivity.
class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NoteAdapter
    private lateinit var noteHelper: NoteHelper

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Notes"

        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)
        adapter = NoteAdapter(this)
        rv_notes.adapter = adapter

        fab_add.setOnClickListener{
            val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD)
        }

        //YANG DIUBAH (UNTUK CONTENT PROVIDER ) ====================================================
        //Menangkap pesan dari NoveProvider
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadNotesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI,true,myObserver)
        /*
        * Di sini kita menciptakan thread baru untuk melihat perubahan (observe)
        * supaya tidak mengganggu kinerja thread utama.
        *  Kemudian membuat sebuah fungsi yang menjadi turunan ContentObserver
        * supaya bisa melakukan fungsi observe. Setelah observer dibuat,
        * kita daftarkan dengan menggunakan registerContentObserver.
        * Maka ketika terjadi perubahan data, kelas onChange akan terpanggil dan melakukan aksi tertentu,
        *  misal di sini yaitu memanggil data lagi supaya data yang ditampilkan di list adalah data terbaru.
        * */
        //==========================================================================================

        if(savedInstanceState == null){
            //Proses Ambil Data
            loadNotesAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null){
                adapter.listNotes = list
            }
        }
    }

    private fun loadNotesAsync(){
        GlobalScope.launch (Dispatchers.Main){
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {

                //YANG DIUBAH (UNTUK CONTENT PROVIDER ) ======================================
                ///Fungsi query() adalah sama dengan query select di dalam database.
                val cursor = contentResolver?.query(CONTENT_URI,null,null,null,null)
                MappingHelper.mapCursorToArrayList(cursor)
                /*
                * Content resolver akan meneruskan obyek Uri tersebut ke content provider dan tentunya akan masuk ke dalam metode query.
                *  Dan jika kita lihat lagi maka obyek Uri dengan nilai CONTENT_URI berarti akan memanggil query select semua data.
                * Maka dari itu kita bisa mendapatkan semua data, namun kita perlu mengubahnya menjadi ArrayList supaya bisa ditampilkan di dalam adapter.
                *  Karena itulah kita memanggil fungsi mapCursorToArrayList untuk convert data dari cursor menjadi ArrayList.
                * */
                //======================= ====================================================
            }
            progressBar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0){
                adapter.listNotes = notes
            }else{
                adapter.listNotes = ArrayList()
                showSnackbarMessage("Tidak Ada Data Saat Ini")
            }
        }
        //Untuk mendapatkan nilai kembaliannya, kita menggunakan fungsi await().
    }

    override fun onSaveInstanceState(outState: Bundle ) {
        super.onSaveInstanceState(outState )
        outState.putParcelableArrayList(EXTRA_STATE,adapter.listNotes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            when(requestCode){
                NoteAddUpdateActivity.REQUEST_ADD -> if (resultCode == NoteAddUpdateActivity.RESULT_ADD){
                    val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)

                    if (note != null) {
                        adapter.addItem(note)
                    }
                    rv_notes.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Satu Item Berhasil ditambahkan")

                }
                NoteAddUpdateActivity.REQUEST_UPDATE ->
                    when(resultCode){
                        NoteAddUpdateActivity.RESULT_UPDATE -> {
                            val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)
                            val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION,0)
                            if (note != null) {
                                adapter.updateItem(position,note)
                            }
                            rv_notes.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu Item Berhasil diubah")

                        }
                        NoteAddUpdateActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION,0)
                            adapter.removeItem(position)
                            showSnackbarMessage("Satu Item Berhasil diHapus")

                        }
                    }
            }
        }

    }
    private fun showSnackbarMessage(message:String){
        Snackbar.make(rv_notes,message,Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        noteHelper.close()
    }

}