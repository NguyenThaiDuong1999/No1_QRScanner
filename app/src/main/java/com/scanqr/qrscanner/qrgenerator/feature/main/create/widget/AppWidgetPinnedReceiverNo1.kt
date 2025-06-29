package com.scanqr.qrscanner.qrgenerator.feature.main.create.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.Toast
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.helper.SharePrefHelper

class AppWidgetPinnedReceiverNo1 : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetId = intent?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            val views = RemoteViews(context.packageName, R.layout.widget_code_preview_no1)


            val imagePath = SharePrefHelper(context).getString("widget")
            val bitmap = BitmapFactory.decodeFile(imagePath)

            if (bitmap != null) {
                views.setImageViewBitmap(R.id.iv_code, bitmap)
            } else {

            }

            if (appWidgetId != null) {
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
        Toast.makeText(context, context.getString(R.string.add_widget_successfully), Toast.LENGTH_SHORT).show()
    }

}