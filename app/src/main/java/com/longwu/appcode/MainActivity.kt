package com.longwu.appcode

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.longwu.appcode.acc.AccActivity
import com.longwu.appcode.adapter.ButtonAdapter
import com.longwu.appcode.adapter.ButtonItem
import com.longwu.appcode.camera.CameraActivity
import com.longwu.appcode.home.HomeWatchActivity
import com.longwu.appcode.jobshdule.BackgroundActivity
import com.longwu.appcode.launcher.LauncherActivity
import com.longwu.appcode.location.LocationActivity
import com.longwu.appcode.notification.NotificationActivity
import com.longwu.appcode.permission.PermissionActivity
import com.longwu.appcode.ui.ArcViewActivity
import com.longwu.appcode.ui.FlatBuffersMapActivity
import com.longwu.appcode.ui.SpeechToTextActivity
import com.longwu.appcode.ui.VulActivity
import com.longwu.appcode.util.ProcessUtil
import com.longwu.appcode.util.SystemInfo
import com.longwu.appcode.wallpaper.LiveWallpaperMainActivity

class MainActivity : AppCompatActivity() {
    private val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonList = listOf(
            ButtonItem("Usage Access") {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//            startActivityForResult(intent,1111)
                ProcessUtil.stopProcess("com.cleanmaster.mguard_cn", this)
//            onJobStartClick()
//            AppWidgetManager.instance.canShowWidget(this, null)
            },
            ButtonItem("Launcher") {
                startActivity(Intent(this, LauncherActivity::class.java))
            },
            ButtonItem("Permission") {
                startActivity(Intent(this, PermissionActivity::class.java))
            },
            ButtonItem("Home Press") {
                startActivity(Intent(this, HomeWatchActivity::class.java))
            },
            ButtonItem("Accessibility") {
                startActivity(Intent(this, AccActivity::class.java))
            },
            ButtonItem("Speech to Text") {
                startActivity(Intent(this, SpeechToTextActivity::class.java))
            },
            ButtonItem("FlatBuffers Map") {
                startActivity(Intent(this, FlatBuffersMapActivity::class.java))
            },
            ButtonItem("Custom View") {
                startActivity(Intent(this, ArcViewActivity::class.java))
            },
            ButtonItem("Live Wallpaper") {
                startActivity(Intent(this, LiveWallpaperMainActivity::class.java))
            },
            ButtonItem("Location") {
                getLocationAndCityName()
            },
            ButtonItem("Notification") {
                startActivity(Intent(this, NotificationActivity::class.java))
            },
            ButtonItem("Camera") {
                startActivity(Intent(this, CameraActivity::class.java))
            },
            ButtonItem("Background Task") {
                startActivity(Intent(this, BackgroundActivity::class.java))
            },
            ButtonItem("Window Manager") {
                windowManagerVul()
            }
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ButtonAdapter(this, buttonList)
    }

    private fun windowManagerVul() {
        Log.e("longlong", "come in ")
        //ActivityManager().isActivityStartAllowedOnDisplay( context,  displayId, Intent intent);
        Handler().postDelayed({
            Log.e("longlong", "start")
            val intent = Intent(this, VulActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }, 5000)
    }

    private fun getLocationAndCityName() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions, 100)
            return
        }
        startActivity(Intent(this, LocationActivity::class.java))
    }

    private fun testConnect(): String {
        var cpu = SystemInfo.getCpuTemp()
        var battery = SystemInfo.getBatteryTemp()
        Log.e("longlong", "cpu=$cpu  battery=$battery")
        return ""
    }

}
