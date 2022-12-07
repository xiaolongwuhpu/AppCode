package com.longwu.appcode.appwidget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.longwu.appcode.R
import android.appwidget.AppWidgetManager
/**
 * @Description: 小组件
 */
class WidgetProvider : AppWidgetProvider() {

    private val TAG: String = WidgetProvider::class.java.simpleName

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?,
    ) {

        appWidgetIds?.also {
            Log.d(
                TAG,
                "onUpdate >> appWidgetIds length : " + it.size
            )
            try {
                for (appWidgetId in it) {
                    Log.i(TAG, "appWidgetId=$appWidgetId")
                    val remoteViews =
                        RemoteViews(context?.packageName, R.layout.layout_desktop_widget)

                    val pendingIntent: PendingIntent? =
                        com.longwu.appcode.appwidget.AppWidgetManager.instance.getJumpIntent(context,
                            "test",
                            123)
                    remoteViews.setOnClickPendingIntent(R.id.llcontent, pendingIntent)

                    Log.i(TAG, "click------> appWidgetId=$appWidgetId")

                    // 清理后累计清理大小
                    remoteViews.setTextViewText(R.id.tvTitle, "widget_text")
                    appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }


    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        Log.d(TAG, "onDeleted")
        super.onDeleted(context, appWidgetIds)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive >> action" + intent?.action)
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context?) {
        Log.d(TAG, "onEnabled")
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context?) {
        Log.d(TAG, "onDisabled")
        super.onDisabled(context)
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        Log.d(TAG, "onRestored")
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?,
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.d(TAG, "onAppWidgetOptionsChanged")
    }
}