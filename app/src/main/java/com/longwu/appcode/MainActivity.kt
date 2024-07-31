package com.longwu.appcode

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.longwu.appcode.acc.AccActivity
import com.longwu.appcode.home.HomeWatchActivity
import com.longwu.appcode.jobshdule.MyJobService
import com.longwu.appcode.launcher.LauncherActivity
import com.longwu.appcode.launcher.LauncherActivity2
import com.longwu.appcode.location.LocationActivity
import com.longwu.appcode.ui.ArcViewActivity
import com.longwu.appcode.ui.FlatBuffersMapActivity
import com.longwu.appcode.ui.MediaBrowserActivity
import com.longwu.appcode.ui.SpeechToTextActivity
import com.longwu.appcode.ui.VulActivity
import com.longwu.appcode.util.ProcessUtil
import com.longwu.appcode.util.SystemInfo
import com.longwu.appcode.util.ThreadUtils
import com.longwu.appcode.wallpaper.LiveWallpaperMainActivity
import kotlinx.android.synthetic.main.activity_main.btn_acc
import kotlinx.android.synthetic.main.activity_main.btn_custom_view
import kotlinx.android.synthetic.main.activity_main.btn_flatbuffers_map
import kotlinx.android.synthetic.main.activity_main.btn_home_press
import kotlinx.android.synthetic.main.activity_main.btn_launcher
import kotlinx.android.synthetic.main.activity_main.btn_permission
import kotlinx.android.synthetic.main.activity_main.btn_speech
import kotlinx.android.synthetic.main.activity_main.btn_widget
import kotlinx.android.synthetic.main.activity_main.btn_window_mananger
import kotlinx.android.synthetic.main.activity_main.liveWallpaper
import kotlinx.android.synthetic.main.activity_main.location
import kotlinx.android.synthetic.main.activity_main.shortcut
import java.util.Timer


class MainActivity : AppCompatActivity() {

    //启动timer 每隔10秒执行一次
    private val timer = Timer()
    private val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_widget.setOnClickListener {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//            startActivityForResult(intent,1111)

            ProcessUtil.stopProcess("com.cleanmaster.mguard_cn", this)
//            onJobStartClick()
//            AppWidgetManager.instance.canShowWidget(this, null)
        }
        btn_launcher.setOnClickListener {
            startActivity(Intent(this, LauncherActivity::class.java))
        }
        btn_permission.setOnClickListener {
            startActivity(Intent(this, LauncherActivity2::class.java))
//            startActivity(Intent(this, PermissionActivity::class.java))
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
        btn_custom_view.setOnClickListener {
            startActivity(Intent(this, ArcViewActivity::class.java))
        }

        liveWallpaper.setOnClickListener {
            startActivity(Intent(this, LiveWallpaperMainActivity::class.java))
        }

        location.setOnClickListener {
            getLocationAndCityName()
        }

        shortcut.setOnClickListener {
            startActivity(Intent(this, MediaBrowserActivity::class.java))
        }

        btn_window_mananger.setOnClickListener {
            Log.e("longlong", "come in ")
//            ActivityManager().isActivityStartAllowedOnDisplay( context,  displayId, Intent intent);
            Handler().postDelayed({
                Log.e("longlong", "start")
                val intent = Intent(this, VulActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }, 5000)
        }


        gonoButton()
    }

    private fun gonoButton() {
        //把所有button gone掉
        //        btn_widget.visibility = View.GONE
        //        btn_launcher.visibility = View.GONE
        //        btn_permission.visibility = View.GONE
        //        btn_home_press.visibility = View.GONE
        //        btn_acc.visibility = View.GONE
        //        btn_speech.visibility = View.GONE
        //        btn_flatbuffers_map.visibility = View.GONE
        //        btn_custom_view.visibility = View.GONE
        //        btn_window_mananger.visibility = View.GONE
    }


    private fun getLocationAndCityName() {
        Log.e("longwu", "getLocationAndCityName------- ${ThreadUtils.isMainThread()}")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //请求定位权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            );
            return
        }
        startActivity(Intent(this, LocationActivity::class.java))
    }

    private fun testConnect(): String {
        var cpu = SystemInfo.getCpuTemp()
        var battery = SystemInfo.getBatteryTemp()
        Log.e("longlong", "cpu=$cpu  battery=$battery")
        var connectivityManager: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network: Network? = connectivityManager.getActiveNetwork()
            val capabilities: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(network)
                    ?: return "";
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return "wifi"
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return "TRANSPORT_ETHERNET"
            }

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return "CONNECTIVITY_MOBILE"
            }

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                return "TRANSPORT_VPN"
            }

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                return "CONNECTIVITY_BLUETOOTH"
            }
        }
        return ""
    }

    private val JOB_INFO_ID = 10001
    private val JOB_PERIODIC = 15 * 60 * 1000L
    private fun onJobStartClick() {
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(this, MyJobService::class.java)
        val jobinfo = JobInfo.Builder(JOB_INFO_ID, componentName)
            .setPeriodic(JOB_PERIODIC)
            .build()
        Log.d("MyJobService", "onJobStartClick");
        jobScheduler.schedule(jobinfo)
    }

}
