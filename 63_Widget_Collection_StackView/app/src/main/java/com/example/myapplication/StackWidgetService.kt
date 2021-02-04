package com.example.myapplication

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService:RemoteViewsService() {
    override fun onGetViewFactory(p0:Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}