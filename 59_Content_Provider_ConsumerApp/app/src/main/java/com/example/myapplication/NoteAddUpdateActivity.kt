package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.db.DatabaseContract
import com.example.myapplication.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.example.myapplication.db.DatabaseContract.NoteColumns.Companion.DATE
import com.example.myapplication.db.NoteHelper
import com.example.myapplication.entity.Note
import com.example.myapplication.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity() , View.OnClickListener{
    //Secara fungsionalitas masih sama akan tetapi kita tidak menggunakan obyek
    // Parcelable untuk ditampilkan di dalam NoteAddUpdateActivity, melainkan menggunakan
    // Uri untuk ambil data kembali dari ContentProvider.
    // Selain itu kita ubah fungsi yang menggunakan NoteHelper menjadi menggunakan ContentProvider.
    private var isEdit = false
    private var note: Note? =  null
    private var position:Int = 0
    private lateinit var noteHelper: NoteHelper
    private lateinit var uriWithid:Uri

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

        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null){
            position = intent.getIntExtra(EXTRA_POSITION,0)
            isEdit = true
        }else{
            note = Note()
        }

        val actionBarTitle: String
        val btnTitle: String


        if (isEdit){

            //YANG DIUBAH (UNTUK CONTENT PROVIDER ) ======================================
            // Uri yang di dapatkan disini akan digunakan untuk ambil data dari provider
            // content://com.example.mynotesapp/note/id
            uriWithid = Uri.parse(CONTENT_URI.toString() + "/" + note?.id)
            val cursor = contentResolver?.query(uriWithid,null,null,null,null)
            if(cursor != null){
                note = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
            /*
            *  Uri yang dibuat adalah parsing penambahan string CONTENT_URI dengan string /id.
            * Ini berarti obyek Uri-nya akan berbentuk seperti ini "content://com.example.mynotesapp/note/id".
            * Dan jika kita lihat lagi maka obyek Uri dengan nilai CONTENT_URI berarti akan memanggil query select dengan id tertentu
            * aka dari itu kita bisa mendapatkan satu data, namun kita perlu mengubahnya menjadi object supaya bisa ditampilkan di dalam teks.
            *  Karena itulah kita memanggil fungsi mapCursorToObject untuk convert data dari cursor menjadi object.
            * */
            //=======================================================================

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
                //YANG DIUBAH (UNTUK CONTENT PROVIDER ) ======================================
                // Gunakan uriWithId untuk update
                // content://com.example.mynotesapp/note/id
                contentResolver.update(uriWithid,values,null,null)
                Toast.makeText(this,"Satu Item Berhasil diEdit",Toast.LENGTH_SHORT).show()
                finish()
                //=======================================================================
            }else{
                note?.date = getCurrentDate()
                values.put(DATE,getCurrentDate())

                //YANG DIUBAH (UNTUK CONTENT PROVIDER ) ======================================
                // Gunakan content uri untuk insert
                // content://com.example.mynotesapp/note
                contentResolver?.insert(CONTENT_URI,values)
                Toast.makeText(this,"Satu Item Berhasil diSimpan",Toast.LENGTH_SHORT).show()
                finish()
                /*
                * Di sini kita memanggil metode insert dengan menggunakan getContentResolver
                * dengan masukan CONTENT_URI dan values yang berisi data. Kemudian fungsi ini akan memanggil
                * metode insert yang ada di kelas NoteProvider.
                * */
                //=======================================================================
            }
        }
    }

    private fun getCurrentDate():String{
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit){
            menuInflater.inflate(R.menu.menu_form,menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete -> showAlertDialog(ALERT_DIALOGE_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOGE_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOGE_CLOSE)
    }

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

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("YA"){ dialog,id  ->
                if(isDialogClose){
                    finish()
                }else{
                    //YANG DIUBAH (UNTUK CONTENT PROVIDER ) ======================================
                    // Gunakan uriWithId dari intent activity ini
                    // content://com.dicoding.picodiploma.mynotesapp/note/id
                    contentResolver.delete(uriWithid,null,null)
                    Toast.makeText(this,"Satu item berhasil dihapus",Toast.LENGTH_SHORT).show()
                    finish()
                    //=======================================================================
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