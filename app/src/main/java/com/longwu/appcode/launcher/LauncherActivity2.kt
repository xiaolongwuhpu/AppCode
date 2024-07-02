package com.longwu.appcode.launcher
import android.content.Intent
import android.os.Bundle
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
        recyclerView?.setLayoutManager(GridLayoutManager(this, 4)) // 4列的网格布局

        loadApps()
        appAdapter = AppAdapter(this, appList)
        recyclerView?.setAdapter(appAdapter)
        appAdapter!!.notifyDataSetChanged()
    }

    private fun loadApps() {
        val pm = packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfos = pm.queryIntentActivities(mainIntent, 0)
        for (resolveInfo in resolveInfos) {
            val packageName = resolveInfo.activityInfo.packageName
            val className = resolveInfo.activityInfo.name
            val appName = resolveInfo.loadLabel(pm).toString()
            val icon = resolveInfo.loadIcon(pm)
            appList += AppInfo(appName, packageName, className, icon)
        }
    }

}