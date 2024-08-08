package com.longwu.appcode.launcher
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.UserHandle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.longwu.appcode.R


class LauncherActivity2 : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var appAdapter: AppAdapter? = null
    private var appList: List<AppInfo> = ArrayList<AppInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView?
        recyclerView?.setLayoutManager(GridLayoutManager(this, 5)) // 5列的网格布局

        loadApps()
        appAdapter = AppAdapter(this, appList)
        recyclerView?.setAdapter(appAdapter)
        appAdapter!!.notifyDataSetChanged()
    }

    private fun loadApps() {
        val pm = packageManager
        val launcherApps = getSystemService(LAUNCHER_APPS_SERVICE) as LauncherApps
        val launcherAppInfos: List<LauncherActivityInfo>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val uid = android.os.Process.myUid()
            launcherAppInfos = launcherApps.getActivityList(null, UserHandle.getUserHandleForUid(uid))
        } else {
            launcherAppInfos = ArrayList()
        }
//        val launcherAppInfos: List<LauncherActivityInfo> =
//            launcherApps.getActivityList(null, UserHandle.getUserHandleForUid())

        for (appInfo in launcherAppInfos) {
            val packageName: String = appInfo.getComponentName().getPackageName()
            val className: String = appInfo.getComponentName().getClassName()
            val appName: String = appInfo.getLabel().toString()
            val icon: Drawable = appInfo.getBadgedIcon(0)
            appList += (AppInfo(appName, packageName, className, icon))
        }

//        val mainIntent = Intent(Intent.ACTION_MAIN, null)
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//        val resolveInfos = pm.queryIntentActivities(mainIntent, 0)
//        for (resolveInfo in resolveInfos) {
//            val packageName = resolveInfo.activityInfo.packageName
//            val className = resolveInfo.activityInfo.name
//            val appName = resolveInfo.loadLabel(pm).toString()
//            val icon = resolveInfo.loadIcon(pm)
//            appList += AppInfo(appName, packageName, className, icon)
//        }
    }

}