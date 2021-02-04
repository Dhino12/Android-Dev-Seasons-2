package com.example.myapplication

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class RandomNumberWidget : AppWidgetProvider() {

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
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)

    // Construct the RemoteViews object
    /*
    * RemoteViews adalah komponen yang dapat kita gunakan untuk mengambil data
    * layout dari widget yang kita pakai. Seperti pada kode setTextViewText()
    * di mana kita mengubah string text pada komponen R.id.appwidget_text.
    * */
    val views = RemoteViews(context.packageName, R.layout.random_number_widget)
    val listUpdate = "Random: " + NumberGenerator.generate(100)
    views.setTextViewText(R.id.appwidget_text, listUpdate)
    //Terjadi update widget yang sebenarnya. Parameter 1 adalah id widget yang ingin kita update,
    // sedangkan parameter 2 adalah RemoteViews yang berisikan views yang telah kita modifikasi.
    //==========================================================================================

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}