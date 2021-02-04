package com.example.myapplication

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.widget.RemoteViews
import android.widget.Toast

class UpdateWidgetService:JobService() {
//Catatan: Jobscheduler hanya support API > 21 maka pastikan minSdkVersion 21 pada build gradle.
// Untuk API < 21 disarankan untuk menggunakan WorkManager

    override fun onStopJob(jobParameters: JobParameters): Boolean = false

    override fun onStartJob(jobParameters: JobParameters) :Boolean {
        //Dengan menggunakan getInstance(this) kita bisa mendapatkan obyek manager dari semua widget pada aplikasi kita.
        val manager = AppWidgetManager.getInstance(this)
        //===============================================================

        val view = RemoteViews(packageName,R.layout.random_number_widget)
        val theWidget = ComponentName(this, RandomNumberWidget::class.java)
        val lastUpdate = "Random: " + NumberGenerator.generate(100)
        view.setTextViewText(R.id.appwidget_text,lastUpdate)
        Toast.makeText(this,"masuk ke Service \n$lastUpdate",Toast.LENGTH_SHORT).show()
        manager.updateAppWidget(theWidget, view)
        //Pada 2 baris kode di atas, kita memperoleh nama widget, yaitu RandomNumbersWidget,
        // kemudian memperbaruinya menggunakan metode updateAppWidget().
        //Berbeda dengan proses update updateAppWidget() pada RandomNumbersWidget,
        // proses update yang dilakukan pada service akan memperbarui semua widget yang ada.
        // Sebabnya, parameter yang dimasukkan adalah ComponentName bukan appWidgetId.
        jobFinished(jobParameters,false)
        return true
    }
}