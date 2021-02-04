package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.work.*
import androidx.work.Data.Builder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() ,View.OnClickListener{

    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOneTimeTask.setOnClickListener(this)
        btnPeriodicTask.setOnClickListener(this)
        btnCancleTask.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
            when(v?.id){
                R.id.btnOneTimeTask -> startOneTimeTask()
                R.id.btnPeriodicTask -> startPeriodicTask()
                R.id.btnCancleTask -> cancelPeriodicTask()
            }
    }

    private fun startOneTimeTask(){
        textStatus.text = getString(R.string.status)
        val data = Builder()
            .putString(MyWorker.EXTRA_CITY,editCity.text.toString())
            .build()

        //================ WorkManager ====================
        //Constraint digunakan untuk memberikan syarat kapan task ini dieksekusi,
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        //======================================
//        OneTimeWorkRequest untuk menjalankan task sekali saja
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
                //Work Manager ==========================
            .setConstraints(constraints)
                //======================================
            .build()

//        Kelebihan dari penggunaan tag yaitu Anda bisa membatalkan task lebih dari satu task sekaligus seperti ini:
//            .addTag("MyTag")

        WorkManager.getInstance().enqueue(oneTimeWorkRequest)
        WorkManager.getInstance()
            .getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this@MainActivity,object : Observer<WorkInfo>{
            override fun onChanged(workInfo:WorkInfo){
                val status = workInfo.state.name
                textStatus.append("\n" + status)
            }
        })
    }

    private fun startPeriodicTask(){
        textStatus.text = getString(R.string.status)

        val data = Builder()
            .putString(MyWorker.EXTRA_CITY,editCity.text.toString())
            .build()

        //Constraint digunakan untuk memberikan syarat kapan task ini dieksekusi,
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

//        PeriodicWorkRequest untuk menjalankan task secara periodic,
//        Kode di atas digunakan untuk menjalankan PeriodicWorkRequest dengan interval 15 menit. Anda bisa mengaturnya dengan mengganti parameter kedua dan ketiga.
        periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
            .setInputData(data)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance().enqueue(periodicWorkRequest)
        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.id).observe(this, object : Observer<WorkInfo>{

//            WorkInfo digunakan untuk mengetahui status task yang dieksekusi,
//            Anda dapat membaca status secara live dengan menggunakan getWorkInfoByIdLiveData
            override fun onChanged(t: WorkInfo?) {
                val status = t?.state?.name
                textStatus.append("\n" + status)
                btnCancleTask.isEnabled = false

                if(t?.state == WorkInfo.State.ENQUEUED){
                    btnCancleTask.isEnabled = true
                }

            }
        })
    }

    private fun cancelPeriodicTask(){
        WorkManager.getInstance().cancelWorkById(periodicWorkRequest.id)
        //Kode di atas digunakan untuk membatalkan task berdasarkan id request.
        //Selain menggunakan id, Anda juga bisa menambahkan tag pada request.
    }
}