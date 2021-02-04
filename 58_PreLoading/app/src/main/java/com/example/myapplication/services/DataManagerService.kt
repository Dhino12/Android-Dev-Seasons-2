package com.example.myapplication.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.database.MahasiswaHelper
import com.example.myapplication.model.MahasiswaModel
import com.example.myapplication.prefs.AppPreference
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.nio.Buffer
import kotlin.coroutines.CoroutineContext

class DataManagerService : Service() , CoroutineScope{
//Di sini Anda menggunakan CoroutineScope dan Job supaya proses yang dilakukan coroutine dapat di-cancel.

    private val TAG = DataManagerService::class.java.simpleName
    private var mActivityMessenger: Messenger? = null
    // membuat fungsi sendMessage dan mengimplementasikannya setelah mendapatkan hasil data dari proses di background thread.
    private lateinit var job:Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    companion object{
        const val PREPARATION_MESSAGE = 0
        const val UPDATE_MESSAGE = 1
        const val SUCCESS_MESSAGE = 2
        const val FAILED_MESSAGE = 3
        const val CANCEL_MESSAGE = 4
        const val ACTIVITY_HANDLER = "activity_handler"
        private const val MAX_PROGRESS = 100.0
    }

//    Pada aplikasi MyPreLoadData ini, Anda menerapkan service pada aplikasi tersebut. Service ini berfungsi untuk memanggil kelas LoadDataAsync.
    override fun onCreate() {
        super.onCreate()
        job = Job() //create the Job
        Log.d(TAG, "onCreate...: ")
    }

    /*
    Ketika semua ikatan sudah di lepas maka ondestroy akan secara otomatis dipanggil
     */
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.d(TAG,"onDestroy: ")
    }

    /*
    Method yang akan dipanggil ketika service diikatkan ke activity
    Kemudian pada bagian onBinder atau ketika service sudah terikat dengan Activity pemanggil, ia akan menjalankan loadData.
    Bagian onUnBind berfungsi untuk melepaskan ikatan kelas DataManagerService dari Activity pemanggil.
    */
    override fun onBind(intent: Intent): IBinder? {
        mActivityMessenger = intent.getParcelableExtra(ACTIVITY_HANDLER)

        loadDataAsync()

        //Proses Ambil Data
        return mActivityMessenger.let { it?.binder }
    }

    /*
    Method yang akan dipanggil ketika service dilepas dari activity
     */
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG,"onUbind...")
        job.cancel() // cancel the Job
        return super.onUnbind(intent)
    }

    /*
    Method yang akan dipanggil ketika service diikatkan kembali
     */
    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG,"onRebind..")
    }

    private fun loadDataAsync(){
        sendMessage(PREPARATION_MESSAGE)
        job = launch {
            val isInsertSuccess = async(Dispatchers.IO){
                getData()
            }
            if (isInsertSuccess.await()){
                sendMessage(SUCCESS_MESSAGE)
            }else{
                sendMessage(FAILED_MESSAGE)
            }
        }
        job.start()
    }

    private fun sendMessage(messageStatus:Int){
        //  Menggunakan bantuan Bundle untuk mengirim value dari service menuju Activity.
        val message = Message.obtain(null,messageStatus)
        try {
            mActivityMessenger?.send(message)
        }catch (e:RemoteException){
            e.printStackTrace()
        }
    }

    private fun getData():Boolean{
        val mahasiswaHelper = MahasiswaHelper.getInstance(applicationContext)
        val appPreference = AppPreference(applicationContext)

        val firstRun = appPreference.firstRun as Boolean
        /*
         * Jika first run true maka melakukan proses pre load,
         * Jika first run false maka akan langsung menuju home
         */
        if (firstRun){
            //Jika firstRun bernilai true, maka akan melakukan perintah insert ke dalam database.
            /*
            Load raw data dari file txt ke dalam array model mahasiswa
            */
            val mahasiswaModels = preLoadRaw()
            mahasiswaHelper.open()
            var progress = 30.0
            publishProgress(progress.toInt())
            val progressMaxInsert = 80.0
            val progerssDiff = (progressMaxInsert - progress) / mahasiswaModels.size
            var isInsertSucess:Boolean
//            Kode di atas akan memasang nilai progress (kemajuan) pada 30%. Kemudian progress akan dihitung dengan
//            cara menemukan nilai progressDiff yang merepresentasikan progress memasukkan data per modelnya.
//            Sebagai contoh, anggap mahasiswaModels berjumlah 5. Maka setiap satu model yang sudah ditambahkan, progress akan bertambah sebesar 10%.

            //======================================================================================
            //Gunakan Ini untuk Insert Query dengan menggunakan standart query
            try {
                mahasiswaHelper.beginTransaction()
                loop@ for (model in mahasiswaModels){
                    when{
                        job.isCancelled -> break@loop
                        else -> {
//                            Setiap proses memasukkan data selesai, maka progress bar perlu diperbarui dengan cara memanggil metode publishProgress.
                            mahasiswaHelper.insertTransaction(model)
                            progress += progerssDiff
                            publishProgress(progress.toInt())
                        }
                    }
                }
                when{
                    job.isCancelled -> {
                        isInsertSucess = false
//                        Dengan memasang nilai false seperti kode di atas, maka ketika aplikasi dibuka kembali,
//                        proses transactional yang telah kita bahas tadi tidak akan dijalankan lagi.
                        appPreference.firstRun = true
                        sendMessage(CANCEL_MESSAGE)
                    }
                    else -> {
//                        Setelah selesai, jalankan setTransactionSucces
                        mahasiswaHelper.setTransactionSuccess()
                        isInsertSucess = true
                        appPreference.firstRun = false
                    }
                }
            }catch (e:Exception){
                Log.e(TAG,"doInBackground: Exception")
                isInsertSucess = false
            }finally {
                mahasiswaHelper.endTransaction()
            }

            // Close helper ketika proses query sudah selesai
            //Akhir dari standart Query
            mahasiswaHelper.close()

            publishProgress(MAX_PROGRESS.toInt())

            return isInsertSucess
        }else{
            //Jika firstRun bernilai false, maka hanya menjalankan progress.
            try {
                synchronized(this){
                    publishProgress(50)
                    publishProgress(MAX_PROGRESS.toInt())
                    return true
                }
            }catch (e:Exception){
                return false
            }
        }
    }


    private fun publishProgress(progress:Int){
        try {
            val message = Message.obtain(null, UPDATE_MESSAGE)
            val bundle = Bundle()
            bundle.putLong("KEY_PROGRESS",progress.toLong())
            message.data = bundle
            mActivityMessenger?.send(message)
        }catch (e:RemoteException){
            e.printStackTrace()
        }
    }

    private fun preLoadRaw():ArrayList<MahasiswaModel>{
        val mahasiswaModels = ArrayList<MahasiswaModel>()
        var line: String?
        val reader: BufferedReader
        try {
            val rawText = resources.openRawResource(R.raw.data_mahasiswa)
            reader = BufferedReader(InputStreamReader(rawText))
            do {
                line = reader.readLine()
                val splitstr = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                // Kode ini akan membaca teks tiap baris dan membaginya berdasarkan "\t" atau tab.
                // Bila kita melihat data mentahnya, maka nama dan nilai id dipisah menggunakan “\t” atau tab.
                //==================================================================================

                //  Nilai nama dan id di atas akan digunakan untuk membuat obyek MahasiswaModel:
                val mahasiswaModel: MahasiswaModel = MahasiswaModel()
                mahasiswaModel.name = splitstr[0]
                mahasiswaModel.nim = splitstr[1]
                mahasiswaModels.add(mahasiswaModel)
            }while (line != null)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return mahasiswaModels
    }
//    Kode di atas mengambil data mentah dari data_mahasiswa. Kemudian tiap baris dari data tersebut akan di-parse.
}
