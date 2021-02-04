package com.example.myapplication

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    /*
    * Catatan: Eksekusi service sesuai dengan nilai yang kita masukkan pada periode tidak dapat kita kontrol penuh,
    *  tetapi akan dijamin oleh scheduler bahwa akan dieksekusi pada interval tersebut.
    * */

    companion object{
        private const val JOB_ID = 100
        private const val SCHEDULE_OF_PERIOD = 8600L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener(this)
        btn_stop.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.btn_start -> startJob()
            R.id.btn_stop -> cancleJob()
        }
    }

    private fun startJob(){
        val mServiceComponent = ComponentName(this, UpdateWidgetService::class.java)

        val builder = JobInfo.Builder(JOB_ID, mServiceComponent)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            builder.setPeriodic(900000) //15 Menit
        } else{
            builder.setPeriodic(SCHEDULE_OF_PERIOD) // 3 menit
        }
        val jobSchedule = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobSchedule.schedule(builder.build())

        Toast.makeText(this,"Job Service Started",Toast.LENGTH_LONG).show()
    }

    private fun cancleJob(){
        val tm = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        tm.cancel(JOB_ID)
        Toast.makeText(this,"Job Service cancled",Toast.LENGTH_SHORT).show()
    }
}

//===================== NOTE ================================
/*
* Sesuaikan nilai pada SCHEDULE_OF_PERIOD dengan mengganti interval waktu yang Anda inginkan,
*  namun hal ini hanya berlaku di Android dengan versi di bawah Nougat. Mulai dari Nougat ke atas,
*  interval dibatasi dengan waktu minimal 15 menit.
* */