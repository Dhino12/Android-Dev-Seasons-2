package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.db.NoteHelper
import com.example.myapplication.entity.Note
import com.example.mynotesapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
        //======================================================
        // memanggil activity
        // tersebut dan mendapatkan nilai result darinya.
        //======================================================
        fab_add.setOnClickListener{
            val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD)
        }

        //======================================================
        //mulai untuk mengambil data dari database dengan memanfaatkan NoteHelper yang sudah kita buat.
        // Sekarang di MainActivity mari kita inisialisasi terlebih dahulu dan panggil metode open()
        // untuk memulai interaksi database dan close() saat activity ditutup supaya tidak terjadi memory leak.

        //Aturan utama dalam penggunaan dan akses database SQLite adalah membuat instance dan membuka koneksi pada metode onCreate():
        //======================================================
        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()
        //======================================================

        //======================================================
        // proses ambil data
//        loadNotesAsync()
        //======================================================
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

    //=================================
    //kita mulai untuk mengambil data dari database dengan menggunakan background thread.
    //=================================
    //Fungsi ini digunakan untuk load data dari tabel dan dan kemudian menampilkannya ke dalam list
    // secara asynchronous dengan menggunakan Background process seperti berikut.
    private fun loadNotesAsync(){
        GlobalScope.launch (Dispatchers.Main){
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
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

    //=================================
    //kita akan  menerapkan metode onSaveInstanceState. Pada metode ini Anda akan menyimpan arraylist,
    // jadi pada saat rotasi, layar berubah dan aplikasi tidak memanggil ulang proses mengambil data dari database.
    // Kemudian pada metode onCreate kita ambil data dari saveInstanceState jika ada.
    //=================================
    override fun onSaveInstanceState(outState: Bundle ) {
        super.onSaveInstanceState(outState )
        outState.putParcelableArrayList(EXTRA_STATE,adapter.listNotes)
    }

    //======================================================
    //Kemudian karena kita sudah membuat halaman NoteAddUpdateActivity untuk menambah dan meng-edit data
    // dengan Intent Result. Di MainActivity kita akan memanggil activity tersebut dan mendapatkan nilai result darinya
    //======================================================
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
                    //Baris di atas akan dijalankan ketika terjadi penambahan data pada NoteAddUpdateActivity.
                    // Alhasil, ketika metode ini dijalankan maka kita akan membuat objek note baru dan inisiasikan dengan getParcelableExtra.
                    // Lalu panggil metode addItem yang berada di adapter dengan memasukan objek note sebagai argumen.
                    // Metode tersebut akan menjalankan notifyItemInserted dan penambahan arraylist-nya.
                    // Lalu objek rvNotes akan melakukan smoothscrolling, dan terakhir muncul notifikasi pesan dengan menggunakan Snackbar.
                }
                NoteAddUpdateActivity.REQUEST_UPDATE ->
                    when(resultCode){
                        NoteAddUpdateActivity.RESULT_UPDATE -> {
                            val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)
                            val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION,0)
//
                            if (note != null) {
                                adapter.updateItem(position,note)
                            }
                            rv_notes.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu Item Berhasil diubah")
                            //Baris di atas akan dijalankan ketika terjadi perubahan data pada NoteAddUpdateActivity.
                            // Prosesnya hampir sama seperti ketika ada penambahan data, tetapi di sini kita harus membuat objek baru yaitu position.
                            // Sebabnya, metode updateItem membutuhkan 2 argumen yaitu position dan Note.
                        }
                        NoteAddUpdateActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION,0)
                            adapter.removeItem(position)
                            showSnackbarMessage("Satu Item Berhasil diHapus")
                            //Baris di atas akan dijalankan jika nilai resultCode-nya adalah RESULT_DELETE.
                            // Di sini kita hanya membutuhkan position karena metode removeItem hanya membutuhkan
                            // position untuk digunakan pada notifyItemRemoved dan penghapusan data pada arraylist-nya.
                        }
                    }
            }
        }
        //===========================================================================================
        //Setiap aksi yang dilakukan pada NoteAddUpdateActivity akan berdampak pada
        // MainActivity baik itu untuk penambahan, pembaharuan atau penghapusan.
        // Metode onActivityResult() akan melakukan penerimaan data dari intent
        // yang dikirimkan dan diseleksi berdasarkan jenis requestCode dan resultCode-nya.
        //===========================================================================================
    }
    private fun showSnackbarMessage(message:String){
        Snackbar.make(rv_notes,message,Snackbar.LENGTH_SHORT).show()
    }
    //==============================================================================================

    //=================================
    // untuk memulai interaksi database dan close() saat activity ditutup supaya tidak terjadi memory leak.
    //Kemudian tutup koneksi pada metode onDestroy() (atau onStop()).
    //=================================
    override fun onDestroy() {
        super.onDestroy()
        noteHelper.close()
    }
    //=================================
}