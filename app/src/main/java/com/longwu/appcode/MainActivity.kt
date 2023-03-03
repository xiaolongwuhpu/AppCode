package com.longwu.appcode

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.longwu.appcode.acc.AccActivity
import com.longwu.appcode.appwidget.AppWidgetManager
import com.longwu.appcode.home.HomeWatchActivity
import com.longwu.appcode.launcher.LauncherActivity
import com.longwu.appcode.permission.PermissionActivity
import com.longwu.appcode.util.SystemInfo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_widget).setOnClickListener(View.OnClickListener {
            AppWidgetManager.instance.canShowWidget(this, null)
        })

        findViewById<Button>(R.id.btn_launcher).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LauncherActivity::class.java))
        })

        findViewById<Button>(R.id.btn_permission).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
        })

        findViewById<Button>(R.id.btn_home_press).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, HomeWatchActivity::class.java))
        })

        findViewById<Button>(R.id.btn_acc).setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this, AccActivity::class.java))
            testCpu()
        })


    }

    private fun testCpu() {
        var cpu = SystemInfo.getCpuTemp()
        var battery = SystemInfo.getBatteryTemp()
        Log.e("longlong", "cpu=$cpu  battery=$battery")
    }

}
