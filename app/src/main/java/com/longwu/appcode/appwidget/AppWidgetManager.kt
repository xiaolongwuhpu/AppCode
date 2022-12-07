package com.longwu.appcode.appwidget

import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.RemoteViews
import com.longwu.appcode.MainActivity
import com.longwu.appcode.R

class AppWidgetManager {
    private val ACTION_APPWIDGET_CREATE_SUCCESS: String = "action_appwidget_create_success"
    private val TAG: String = AppWidgetManager::class.java.simpleName

    var actToStop = false

    companion object {
        val instance: AppWidgetManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppWidgetManager()
        }
    }

    fun canShowWidget(context: Context, backListener: OnWidgetEventListener? = null): Boolean {
        Log.e(TAG, "check widget..........")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDeskTopWidget(context, backListener)
        } else {
            Log.e(TAG, "device is low version")
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDeskTopWidget(
        context: Context,
        backListener: OnWidgetEventListener?,
    ): Boolean {

        val appWidgetManager = context.getSystemService(
            android.appwidget.AppWidgetManager::class.java
        )
        if (!appWidgetManager.isRequestPinAppWidgetSupported) {
            Log.e(TAG, "device is not Supported widget !!!")
            return false
        }
        setWidgetCreateReceiver(context)
        val componentName = ComponentName(
            context,
            WidgetProvider::class.java
        )
        val intent = Intent()
        intent.action = ACTION_APPWIDGET_CREATE_SUCCESS
        val successCallback = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val b = appWidgetManager.requestPinAppWidget(componentName, null, successCallback)
        if (b) {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    if (actToStop) {
                        actToStop = false
                    } else {
                        Log.i(TAG, "not suport device ，fitter report ..........................")
                        backListener?.also {
                            it.onWidgetBackEvent()
                        }
                    }
                },
                400L
            )

        }
        return true
    }

    private fun setWidgetCreateReceiver(context: Context) {
        val receiver = WidgetCreateReceiver()
        val inflater = IntentFilter()
        inflater.addAction(ACTION_APPWIDGET_CREATE_SUCCESS)
        context.registerReceiver(receiver, inflater)
    }

    class WidgetCreateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(
                "AppWidgetManager",
                "WidgetCreateReceiver received! action: [${intent.action}], flags: [${intent.flags}]"
            )
        }
    }

    private fun widgetExists(context: Context?): Boolean {
        context?.also {
            val myWidget = ComponentName(context, WidgetProvider::class.java)
            val ids: IntArray =
                android.appwidget.AppWidgetManager.getInstance(context).getAppWidgetIds(myWidget)
            return ids.isNotEmpty()
        }
        return false
    }

    interface OnWidgetEventListener {
        fun onWidgetBackEvent()
    }

    fun updateWidget(context: Context?, isUpdate: Boolean) {
        if (context == null) {
            return
        }
        try {
            //获取Widgets管理器
            val widgetManager =
                android.appwidget.AppWidgetManager.getInstance(context.applicationContext)
            //widgetManager所操作的Widget对应的远程视图即当前Widget的layout文件
            val remoteView =
                RemoteViews(context.packageName, R.layout.layout_desktop_widget)
            remoteView.setTextViewText(R.id.tvTitle, "小组件text")
            val componentName =
                ComponentName(context.applicationContext, WidgetProvider::class.java)

            val pendingIntent: PendingIntent? = getJumpIntent(context, "test", 123)
            remoteView.setOnClickPendingIntent(R.id.llcontent, pendingIntent)

            widgetManager.updateAppWidget(componentName, remoteView)
            Log.i("AppWidgetManager", "更新小组件数据.................................")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getJumpIntent(context: Context?, functionType: String, requestCode: Int): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        var pendingIntent: PendingIntent?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }
        return pendingIntent
    }

}