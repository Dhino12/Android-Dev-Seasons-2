package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mynotesapp.db.DatabaseContract
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.example.myapplication.db.NoteHelper
import com.example.myapplication.entity.Note
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity() , View.OnClickListener{
    private var isEdit = false
    private var note: Note? =  null
    private var position:Int = 0
    private lateinit var noteHelper: NoteHelper

    companion object{
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOGE_CLOSE = 10
        const val ALERT_DIALOGE_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        //==============================================================
        //mulai implementasikan onCreate mulai dari panggil instance sampai menerapkan data hasil dari intent.
        //==============================================================
        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null){
            position = intent.getIntExtra(EXTRA_POSITION,0)
            isEdit = true
            Toast.makeText(this,"Masuk IF favoriteItems",Toast.LENGTH_SHORT).show()
        }else{
            note = Note()
        }

        val actionBarTitle: String
        val btnTitle: String

        //Variable isEdit akan menjadi true pada saat Intent melalui kelas adapter, karena mengirimkan objek listnotes.
        //Lalu pada NoteAddUpdateActivity akan divalidasi. Jika tidak null maka isEdit akan berubah true
        if (isEdit){
            actionBarTitle = "UBAH"
            btnTitle = "Update"

            note?.let {
                edt_title.setText(it.title)
                edt_description.setText(it.description)
            }
        }else{
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.text = btnTitle
        //==============================================================
        //Di sini ketika data berhasil ditambahka ke database,
        // kita akan mengembalikan nilai dengan menggunakan setResult,
        // hal ini sama dengan saat kita belajar di materi Intent dengan ResultActivity.
        //==============================================================
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if(view.id == R.id.btn_submit){
            val title = edt_title.text.toString().trim()
            val description =  edt_description.text.toString().trim()

            if(title.isEmpty()){
                edt_title.error = "Field can not be blank"
                return
            }

            note?.title = title
            note?.description = description

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE,note)
            intent.putExtra(EXTRA_POSITION,position)

            val values = ContentValues()
            values.put(DatabaseContract.NoteColumns.TITLE, title)
            values.put(DatabaseContract.NoteColumns.DESCRIPTION,description)

            if (isEdit) {
                val result = noteHelper.update(note?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@NoteAddUpdateActivity,
                        "Gagal Mengupdate Data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                note?.date = getCurrentDate()
                values.put(DATE,getCurrentDate())
                val result = noteHelper.insert(values)
                Toast.makeText(this,"isi result: $result posisi: $position",Toast.LENGTH_SHORT).show()

                if(result > 0){
                    note?.id = result.toInt()
                    setResult(RESULT_ADD,intent)
                    finish()
                }else{
                    Toast.makeText(this,"Gagal Menambah Data",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //=======================================================
    // metode getCurrentDateuntuk mengambil tanggal dan jam.
    //=======================================================
    private fun getCurrentDate():String{
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    //=======================================================
    //metode onCreateOptionsMenu untuk memanggil menu_form.xml.
    //Ketika pengguna berada pada proses pembaruan data,
    // setiap kolom pada form sudah terisi otomatis. Ikon untuk hapus di sudut kanan atas ActionBar
    // berfungsi untuk menghapus data. Kode berikut akan menjalankan kebutuhan di atas.
    // Intinya cek nilai boolean isEdit yang berasal dari proses validasi, apakah objek note berisi null atau tidak.
    //=======================================================
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit){
            menuInflater.inflate(R.menu.menu_form,menu)
        }
        return super.onCreateOptionsMenu(menu)
    }
    //=======================================================

    //=======================================================
    // metode onOptionsItemSelecteduntuk memberikan fungsi ketika menu diklik.
    //=======================================================
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete -> showAlertDialog(ALERT_DIALOGE_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOGE_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }
    //=======================================================

    //=======================================================
    //Pada saat menekan tombol back (kembali), kita ingin memunculkan AlertDialog. Panggil metode onBackPressed.
    //=======================================================
    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOGE_CLOSE)
    }
    //=======================================================

    //=======================================================
    // metode showAlertDialog untuk memunculkan dialognya dan mengembalikan
    // nilai result untuk diterima halaman MainActivity nantinya.
    //=======================================================
    private fun showAlertDialog(type:Int){
        val isDialogClose = type == ALERT_DIALOGE_CLOSE
        val dialogTitle:String
        val dialogMessage:String

        if(isDialogClose){
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form ? "
        }else{
            dialogMessage = "Apakah anda ingin menghapus item ini ?"
            dialogTitle = "Hapus Note"
        }

        //Pada proses penghapusan data, dialog konfirmasi tampil.
        // Ia pun muncul ketika pengguna menekan tombol back baik pada ActionBar atau peranti.
        // Dialog konfirmasi tersebut muncul sebelum menutup halaman. Untuk itu, gunakan fasilitas AlertDialoguntuk menampilkan dialog.
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("YA"){
                dialog,id  -> if(isDialogClose){
                finish()
            }else{
                    val reslut = noteHelper.deleteById(note?.id.toString()).toLong()
                    if(reslut > 0){
                        //Setiap aksi akan mengirimkan data dan RESULT_CODE untuk diproses pada MainActivity, contohnya seperti ini:
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION,position)
                        setResult(RESULT_DELETE,intent)
                        Toast.makeText(this,"posisi: ${note?.id.toString()} name: ${note?.title.toString()}",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this@NoteAddUpdateActivity,"Gagal menghapus data",Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Tidak"){dialog,id -> dialog.cancel()}
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}

/*
Tanggung jawab utama NoteAddUpdateActivity adalah sebagai berikut:
[ - ] Menyediakan form untuk melakukan proses input data.

[ - ] Menyediakan form untuk melakukan proses pembaruan data.

[ - ] Jika pengguna berada pada proses pembaruan data maka setiap kolom pada form sudah terisi otomatis
        dan ikon untuk hapus yang berada pada sudut kanan atas ActionBar ditampilkan dan berfungsi untuk menghapus data.

[ - ] Sebelum proses penghapusan data, dialog konfirmasi akan tampil. Pengguna akan ditanya terkait penghapusan
        yang akan dilakukan.

[ - ] Jika pengguna menekan tombol back (kembali) baik pada ActionBar maupun peranti, maka akan tampil dialog
        konfirmasi sebelum menutup halaman.

[ - ] Masih ingat materi di mana sebuah Activity menjalankan Activity lain dan menerima nilai balik pada
        metode onActivityResult()? Tepatnya di Activity yang dijalankan dan ditutup dengan menggunakan
        parameter REQUEST dan RESULTCODE. Jika Anda lupa, baca kembali baca modul 1 tentang Intent ya.

* */