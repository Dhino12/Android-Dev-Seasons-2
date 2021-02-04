package com.example.myapplication

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri

/**
 * Implementation of App Widget functionality.
 */
class ImageBannerWidget : AppWidgetProvider() {

    companion object{
        private const val TOAST_ACTION = "com.example.TOAST_ACTION"
        const val EXTRA_ITEM = "com.example.EXTRA_ITEM"

        //pindahkan fungsi ini ke companion object, karena kita akan memanggil fungsi ini dari luar kelas
        private fun updateAppWidget(context: Context,appWidgetManager: AppWidgetManager,appWidgetId: Int){
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            /*
            * Kita memasang RemoteAdapter ke dalam widget dengan menggunakan obyek
            * Intent dan nilai id dari RemoteView yaitu stack_view
            * */
            val view = RemoteViews(context.packageName,R.layout.image_banner_widget)
            view.setRemoteAdapter(R.id.stack_view,intent)
            view.setEmptyView(R.id.stack_view,R.id.empty_view)

            /*
            * Kita menjalankan getBroadcast() untuk melakukan proses broadcast ketika salah satu widget ditekan.
            * */
            val toastIntent = Intent(context, ImageBannerWidget::class.java)
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(context,0,toastIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            view.setPendingIntentTemplate(R.id.stack_view,toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId,view)
        }

    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    //==============================================================================
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        /*
        * percabangan digunakan untuk membedakan action yang terjadi.
        * Kita dapat mengambil data action tersebut dengan memanfaatkan extra dari sebuah intent.
        * */
        if (intent?.action != null){
            if (intent.action == TOAST_ACTION){
                val viewIndex = intent.getIntExtra(EXTRA_ITEM,0)
                Toast.makeText(context,"Touched View $viewIndex",Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Kode di atas akan dijalankan ketika widget ditekan.
    //==============================================================================
}