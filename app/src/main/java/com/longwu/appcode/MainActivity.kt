package com.longwu.appcode

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.longwu.appcode.acc.AccActivity
import com.longwu.appcode.appwidget.AppWidgetManager
import com.longwu.appcode.home.HomeWatchActivity
import com.longwu.appcode.launcher.LauncherActivity
import com.longwu.appcode.permission.PermissionActivity
import com.longwu.appcode.ui.FlatBuffersMapActivity
import com.longwu.appcode.ui.SpeechToTextActivity
import com.longwu.appcode.util.SystemInfo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_widget.setOnClickListener {
            AppWidgetManager.instance.canShowWidget(this, null)
        }
        btn_launcher.setOnClickListener {
            startActivity(Intent(this, LauncherActivity::class.java))
        }
        btn_permission.setOnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
        }
        btn_home_press.setOnClickListener {
            startActivity(Intent(this, HomeWatchActivity::class.java))
        }
        btn_acc.setOnClickListener {
            startActivity(Intent(this, AccActivity::class.java))
        }
        btn_speech.setOnClickListener {
            startActivity(Intent(this, SpeechToTextActivity::class.java))
        }
        btn_flatbuffers_map.setOnClickListener {
            startActivity(Intent(this, FlatBuffersMapActivity::class.java))
        }

    }

    private fun testCpu() {
        var cpu = SystemInfo.getCpuTemp()
        var battery = SystemInfo.getBatteryTemp()
        Log.e("longlong", "cpu=$cpu  battery=$battery")
    }

}
