package com.example.myapplication

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */
class RandomNumberWidget : AppWidgetProvider() {

    companion object{
        private const val WIDGET_CLICK = "widgetsclick"
        private const val WIDGET_ID_EXTRA = "widget_id_extra"
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.random_number_widget)
        val lastUpdate = "Random: " + NumberGenerator.generate(100)
        views.setTextViewText(R.id.appwidget_text, lastUpdate)

        /*
        * Pada baris kode di atas parameter kedua merupakan PendingIntent yang didapatkan dari getPendingSelfIntent().
        * Jadi ketika views dengan id btn_click diklik, ia akan menjalankan PendingIntent yang diset.
        * */
        views.setOnClickPendingIntent(R.id.btn_click, getPendingSelfIntent(context, appWidgetId, WIDGET_CLICK))
        /*
        * Pada obyek Intent, kita menambahkan parameter extra berupa appWidgetId.
        * Ini dimaksudkan agar kita mengetahui widget mana yang ditekan dengan menggunakan appWidgetId sebagai identifier-nya.
        * Apa yang di-broadcast tentunya harus ada receiver-nya. Maka dari itu kita melakukan metode override onReciever() pada AppWidgetProvider.
        * */

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        /*
        * Metode onUpdate() adalah metode yang akan dipanggil ketika widget pertama kali dibuat.
        * Metode ini juga akan dijalankan ketika updatePeriodMillis yang di dalam random_numbers_widget_info.xml mencapai waktunya.
        * */
        //=========================================================================================
        /*
        * Perulangan di sini dimaksudkan untuk menentukan widget mana yang akan di-update karena
        * jumlah widget dalam sebuah aplikasi bisa lebih dari 1. Jadi,
        * kita perlu mendefinisikan widget mana yang perlu diperbarui oleh sistem.
        * */
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
            //Metode di atas adalah metode yang dipanggil di setiap perulangan appWidgetIds
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        /*
        * metode onUpdate(), ia dijalankan secara otomatis.
        *  Sedangkan metode onReceive() dijalankan ketika aksi klik pada komponen telah di-broadcast.
        * */
        if(WIDGET_CLICK == intent?.action){
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val views = RemoteViews(context?.packageName,R.layout.random_number_widget)
            val lastUpdate = "Random: " + NumberGenerator.generate(100)
            val appWidgetId =  intent.getIntExtra(WIDGET_ID_EXTRA, 0)
            Toast.makeText(context,"Tertekan $lastUpdate",Toast.LENGTH_SHORT).show()
            views.setTextViewText(R.id.appwidget_text,lastUpdate)
            appWidgetManager.updateAppWidget(appWidgetId,views)
        }
    }

    private fun getPendingSelfIntent(context: Context,appWidgetId: Int, action:String):PendingIntent{
        val intent = Intent(context,javaClass)
        intent.action = action
        intent.putExtra(WIDGET_ID_EXTRA,appWidgetId)
        return PendingIntent.getBroadcast(context, appWidgetId, intent,0)
    }

}
