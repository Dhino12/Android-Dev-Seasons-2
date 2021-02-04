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

//Skenario pada aplikasi di atas adalah membuat sebuah proses terjadwal (scheduler task) untuk
// mengunduh data cuaca per 3 jam sekali. Untuk memudahkan pemeriksaan,
// maka selama proses pengembangan, cuaca akan ditampilkan selama 3 menit sekali.
class MainActivity : AppCompatActivity() ,View.OnClickListener {

    companion object{
        private const val JOB_ID = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener(this)
        btn_cancle.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_start -> startJob()
            R.id.btn_cancle -> cancleJob()
        }
    }

    //Metode onStartJob adalah metode yang akan dipanggil ketika scheduler berjalan.
    private fun startJob(){
        if(isJobRunning(this)){
            Toast.makeText(this,"Job Service is Already scheduled",Toast.LENGTH_SHORT).show()
            return
        }

        val mServiceComponent = ComponentName(this, GetCurrentWeatherJobService::class.java)
        val builder = JobInfo.Builder(JOB_ID,mServiceComponent)

        //============================================================================
            /*
        Kondisi network,
        NETWORK_TYPE_ANY, berarti tidak ada ketentuan tertentu
        NETWORK_TYPE_UNMETERED, adalah network yang tidak dibatasi misalnya wifi
        */
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        //============================================================================
        //============================================================================
        /*
            Kondisi device, secara default sudah pada false
            false, berarti device tidak perlu idle ketika job ke trigger
            true, berarti device perlu dalam kondisi idle ketika job ke trigger
            */
        builder.setRequiresDeviceIdle(false)
        //============================================================================
        /*
            Kondisi charging
            false, berarti device tidak perlu di charge
            true, berarti device perlu dicharge
            */
        builder.setRequiresCharging(false)
        //============================================================================

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

//            setPeriodic, set berapa interval waktu kapan job akan dijalankan. Ini bisa kita gunakan untuk menjalankan job yang sifatnya repeat atau berulang.
            builder.setPeriodic(900000) //15 Menit

        }else {
            builder.setPeriodic(180000) //3 Menit
        }
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(builder.build())
        Toast.makeText(this,"Job Service Started",Toast.LENGTH_SHORT).show()
    }

    private fun cancleJob(){
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(JOB_ID)
        Toast.makeText(this,"Job Service Cancled",Toast.LENGTH_SHORT).show()
    }

    private fun isJobRunning(context: Context): Boolean{
        var isScheduled = false
        val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        for (jobInfo in scheduler.allPendingJobs){
            if(jobInfo.id == JOB_ID){
                isScheduled = true
                break
            }
        }
        return isScheduled
    }
    //Fungsi di atas digunakan untuk mengecek apakah job sudah berjalan atau belum,
    // sehingga job tidak dibuat secara berulang-ulang. Fungsi tersebut dipanggil di dalam startJob.
}