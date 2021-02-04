package com.example.myapplication

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStreamWriter

internal object FileHepler{
    private val TAG = FileHepler::class.java.name

    fun writeToFile(fileModel: FileModel, context: Context){
        try {
            //=========================================================================
            //fileModel.filename.toString()
            // menyimpan data yang bertipekan string ke dalam sebuah berkas pada internal storage.
            val outputStreamWrite = OutputStreamWriter(context.openFileOutput(fileModel.fileName.toString(),Context.MODE_PRIVATE))
            //Dengan menggunakan komponen OutputStreamWriter, Anda dapat menulis data ke dalam berkas menggunakan stream.
            //Anda menggunakan metode openFileOutput() untuk membuka berkas sesuai dengan namanya.
            //Untuk menggunakan method openFileOutput() Anda harus mengetahui context aplikasi
            // yang menggunakannya. Oleh karena itu, dalam metode ini Anda memberikan inputan parameter context.
            //=========================================================================

            outputStreamWrite.write(fileModel.data.toString())
            outputStreamWrite.close()
        }catch (e:IOException){
            Log.e(TAG,"File write failed: ", e)
        }
    }

    fun readFromFile(context: Context,fileName:String): FileModel {
        val fileModel = FileModel()
        try {
            val inputStream = context.openFileInput(fileName)
            if (inputStream != null){
                // =========================================================
                // Pada metode readFromFile(), kita menggunakan komponen InputStreamReader.
                // Namun logikanya masih sama. Data pada berkas akan dibaca menggunakan stream.
                // Data pada tiap baris dalam berkas akan mampu diperoleh dengan menggunakan bufferedReader.
                // =========================================================
                val receiveString  = inputStream.bufferedReader().use(BufferedReader::readText)
                inputStream.close()
                fileModel.data = receiveString
                fileModel.fileName = fileName
            }
        }catch (e:FileNotFoundException){
            Log.e(TAG,"File Not Found: ",e)
        }catch (e:IOException){
            Log.e(TAG,"Can Not Read File: ",e)
        }
        return fileModel
    }

    //    Pada kedua contoh di atas, metode writeToFile dan readFromFile berbentuk static. Karena sifatnya yang static,
    //    maka kedua metode tersebut dapat dipanggil tanpa menginisiasi kelas yang memilikinya.
    //===============================================================================================================
    //    val fileModel = FileHelper.readFromFile(this, title)
    //------------------------------------------------------------
    //    Kode di atas adalah contoh tambahan bagaimana metode static dapat dipanggil tanpa menginisasi kelas yang memilikinya.
    //    Metode readFromFile dapat dijalankan tanpa menginisiasi kelas FileHelper
    //================================================================================================================
}