package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var path:File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_new.setOnClickListener(this)
        button_open.setOnClickListener(this)
        button_save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.button_new -> newFile()
            R.id.button_open -> showList()
            R.id.button_save -> saveFile()
        }
    }

    //===========================================================
    //Membuat Text Baru
    //===========================================================
    private fun newFile(){
        edit_title.setText("")
        edit_file.setText("")
        Toast.makeText(this,"Clearing File",Toast.LENGTH_SHORT).show()
        // Proses new/clear file di sini sangat sederhana. Yaitu dengan mengosongkan title,
        // konten dan kemudian menampilkan toast.
    }

    //===========================================================
    //Membuka dan Menampilkan berkas
    //===========================================================
    private fun showList(){
        val arrayList = ArrayList<String>()

        //=========================================================
        // Anda dapat melihat kode untuk menampilkan data internal storage pada baris ini.
        val path:File = filesDir
        Collections.addAll(arrayList,*path.list() as Array<String>)
        // Obyek filesDir() akan secara otomatis memperoleh path dari internal storage aplikasi Anda. Dengan menggunakan .list(),
        // Anda akan memperoleh semua nama berkas yang ada. Tiap berkas yang ditemukan ditambahkan ke dalam obyek arrayList.
        //=========================================================

        val items = arrayList.toTypedArray()

        // ==================================================================================
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih File yang diinginkan")
        builder.setItems(items){ dialog, item -> loadData(items[item].toString())}
        val alert = builder.create()
        alert.show()
        // Dengan memanfaatkan AlertDialog pada beberapa baris di atas,
        // Anda dapat membuat daftar pilihan berkas sederhana. Setelah salah satu berkas
        // pada daftar tersebut di pilih, maka aplikasi akan memanggil metode loadData().
        //----------------------------------------------------------------------------------
        //  val fileModel = FileHepler.readFromFile(this,title)
        //  Anda dapat menggunakan perolehan data untuk mengisi editContent.
        // ==================================================================================
    }

    private fun loadData(title:String){
        val fileModel = FileHepler.readFromFile(this,title)
        edit_title.setText(fileModel.fileName)
        edit_file.setText(fileModel.data)
        Toast.makeText(this,"Loading " + fileModel.fileName + " data",Toast.LENGTH_SHORT).show()
    }

    //===========================================================
    //Menyimpan Berkas
    //===========================================================
    private fun saveFile(){
        when{
            //  Kita melakukan pemeriksaan apakah judul kosong karena membutuhkan nama ketika hendak menyimpan data.
            edit_title.text.toString().isEmpty() -> Toast.makeText(this,"Title harus diisi terlebih dahulu",Toast.LENGTH_SHORT).show()
            edit_file.text.toString().isEmpty() -> Toast.makeText(this,"Content harus diisi terlebih dahulu",Toast.LENGTH_SHORT).show()
            else -> {
                val title = edit_title.text.toString()
                val text = edit_file.text.toString()
                val fileModel = FileModel()
                fileModel.fileName = title
                fileModel.data = text

                //=============================================================================
                //  Dengan menyediakan nama file, konten, dan context, Anda dapat langsung menyimpan data dengan memanfaatkan FileHelper Anda.
                FileHepler.writeToFile(fileModel,this)
                //------------------------------------------------------------------------------
                //=============================================================================

                Toast.makeText(this,"Saving" + fileModel.fileName + " file",Toast.LENGTH_SHORT).show()
            }
        }
    }

}