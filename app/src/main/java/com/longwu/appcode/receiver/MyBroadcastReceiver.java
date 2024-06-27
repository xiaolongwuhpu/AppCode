package com.longwu.appcode.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RESTARTED", "MyBroadcastReceiver onReceive");
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d("RESTARTED", "Package Restarted: " + packageName);
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED)) {
            // 获取广播中传递的包名
            String packageName = intent.getData().getSchemeSpecificPart();

            // 获取最近一次退出的应用程序包名
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RecentTaskInfo> recentTaskInfoList = activityManager.getRecentTasks(1, ActivityManager.RECENT_WITH_EXCLUDED);
            if (recentTaskInfoList != null && !recentTaskInfoList.isEmpty()) {
                ActivityManager.RecentTaskInfo recentTaskInfo = recentTaskInfoList.get(0);
                Intent lastIntent = recentTaskInfo.baseIntent;
                ComponentName componentName = lastIntent.getComponent();
                String lastPackageName = componentName.getPackageName();

                // 判断最近一次退出的应用程序是否是当前应用程序
                if (!lastPackageName.equals(packageName)) {
                    // 最近一次退出的应用程序不是当前应用程序，可以在这里进行相应的操作
                    Log.d("RESTARTED", "Exit from " + lastPackageName + " to " + packageName);
                }
            }
        }
    }
}