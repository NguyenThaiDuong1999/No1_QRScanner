package com.scanqr.qrscanner.qrgenerator.feature.main.create.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import com.scanqr.qrscanner.qrgenerator.R
import com.scanqr.qrscanner.qrgenerator.helper.SharePrefHelper

class CodeWidgetProviderNo1 : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for (appWidgetId in appWidgetIds) {
            val imagePath = getImagePath(context)
            if (imagePath != null) {
                updateAppWidgetWithImage(context, appWidgetManager, appWidgetId, imagePath)
            }
        }
    }

    private fun getImagePath(context: Context): String? {
        return SharePrefHelper(context).getString("widget")
    }

    private fun updateAppWidgetWithImage(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, imagePath: String) {
        val views = RemoteViews(context.packageName, R.layout.widget_code_preview_no1)

        val bitmap = BitmapFactory.decodeFile(imagePath)
        views.setImageViewBitmap(R.id.iv_code, bitmap)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}