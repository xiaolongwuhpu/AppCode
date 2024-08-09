package com.longwu.appcode.util;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProcessUtil {


    void getRunningAPK(Context context) {
        PackageManager pm = context.getPackageManager();
        for (PackageInfo lp : pm.getInstalledPackages(0)){
            if (((ApplicationInfo.FLAG_SYSTEM & lp.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & lp.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_STOPPED & lp.applicationInfo.flags) == 0)) {
                //正在运行的三方app进程信息
                Log.e("ProcessUtil", "packageName=" + lp.packageName
                        + "  processName=" + lp.applicationInfo.processName);
            }
        }
    }


    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {


        PackageManager pm = context.getPackageManager();
        for (PackageInfo lp : pm.getInstalledPackages(0)) {
            if (((ApplicationInfo.FLAG_SYSTEM & lp.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & lp.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_STOPPED & lp.applicationInfo.flags) == 0)) {
                //正在运行的三方app进程信息
                Log.e("ProcessUtil", "packageName=" + lp.packageName
                        + "  processName=" + lp.applicationInfo.processName);
            }
        }


//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
//            Log.d("ProcessUtil", "isAppRunning runningTasks.size: " + runningTasks.size());
//        for (ActivityManager.RunningTaskInfo task : runningTasks) {
//            String pkg = task.baseActivity.getPackageName();
//            Log.d("ProcessUtil", "isAppRunning pkg: " + pkg);
////            runningApps.add(new RunningAppInfo(packageName, true));
//        }
//
//        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
//        Log.d("ProcessUtil", "isAppRunning runningServices.size: " + runningTasks.size());
//        for (ActivityManager.RunningServiceInfo service : runningServices) {
//            String pkg = service.service.getPackageName();
//            Log.d("ProcessUtil", "isAppRunning runningServices pkg: " + pkg);
////            if (!runningApps.contains(new RunningAppInfo(packageName, false))) {
////                runningApps.add(new RunningAppInfo(packageName, false));
////            }
//        }


        return false;

//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
//        Log.d("ProcessUtil", "isAppRunning size: " + list.size());
//        if (list.size() <= 0) {
//            return false;
//        }
//        for (ActivityManager.RunningTaskInfo info : list) {
//            Log.d("ProcessUtil", "isAppRunning getPackageName: " + info.baseActivity.getPackageName());
//            if (info.baseActivity.getPackageName().equals(packageName)) {
//                return true;
//            }
//        }
//        return false;


//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (activityManager != null) {
//            // 获取活动任务列表（Recent Tasks）
//            int maxNumTasks = 50; // 最大获取任务数量
//            List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
//            Log.d("ProcessUtil", "isAppRunning taskPackageName.size : " + appTasks.size());
//            for (ActivityManager.AppTask task : appTasks) {
//                if (task != null && task.getTaskInfo() != null && task.getTaskInfo().baseIntent != null) {
//                    String taskPackageName = task.getTaskInfo().baseIntent.getComponent().getPackageName();
//                    Log.d("ProcessUtil", "isAppRunning taskPackageName : " + taskPackageName);
//                    if (packageName.equals(taskPackageName)) {
//                        return true; // 应用正在运行
//                    }
//                }
//                if (--maxNumTasks <= 0) {
//                    break; // 已经检查了最大数量的任务
//                }
//            }
//        }
//        return false; // 应用未在运行


//        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//        long time = System.currentTimeMillis()-10000;
//        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time, time+10000);
//        Log.d("ProcessUtil", "isAppRunning usageStatsList.size=  " + usageStatsList.size());
//        for (UsageStats usageStats : usageStatsList) {
//            String packageName2 = usageStats.getPackageName();
//            boolean b = usageStats.getLastTimeStamp() > time;
//            long diff = usageStats.getLastTimeStamp() - time;
//            if (packageName2.equals(packageName) && b) {
//            Log.d("ProcessUtil", "isAppRunning packageName2=  " + packageName2 + "  usageStats.getLastTimeStamp()=  " + usageStats.getLastTimeStamp() + "  diff=  " + diff);
//                // 应用在前台运行
//                return true;
//            }
//        }
//        return false;
    }


    public static String TAG = "ProcessUtil";

    //验证可用
    public static void stopProcess(String processName, Context context) {
        Log.d(TAG, "into stopProcess ==>>processName ==>>" + processName);
        PackageInfo pi = new PackageInfo();
        ActivityManager am = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        }
        List<ActivityManager.RunningAppProcessInfo> runs = am.getRunningAppProcesses();
        Method forceStopPackage = null;
        try {
            forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
            Log.d(TAG, "into kill ==>>" + processName + "==forceStopPackage=>>" + forceStopPackage);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, processName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.d(TAG, "error00 ==>>" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.d(TAG, "error11 ==>>" + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.d(TAG, "error22 ==>>" + e.getMessage());
        }

//
//        Method method = null;
//        try {
//            method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            method.invoke(am, processName); //packageName是需要强制停止的应用程序包名
//        } catch (IllegalAccessException e) {
////            throw new RuntimeException(e);
//            Log.d(TAG, "error33 ==>>" + e.getMessage());
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
////            throw new RuntimeException(e);
//            Log.d(TAG, "error44 ==>>" + e.getMessage());
//            e.printStackTrace();
//        }


        killAssignPkg(processName, context);

//        final List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(20 + 1, ActivityManager.RECENT_IGNORE_UNAVAILABLE); //MAX_RECENT_TASKS=20
//        for (ActivityManager.RecentTaskInfo taskInfo : recentTasks) {
//            if (taskInfo.baseActivity.getPackageName().equals(processName)) {//pkgName是要移除的应用的包名
//                try {
//                    ActivityManagerNative.getDefault().removeTask(taskInfo.id);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//        }
    }

    private static void killAssignPkg(String processName, Context context){
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Method method = null;
        try {
            method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(mActivityManager, processName);
            Log.d(TAG, "error55 ==>>》》》》》》");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.d(TAG, "error66 ==>>" + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "error77 ==>>" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.d(TAG, "error88 ==>>" + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.d(TAG, "error99 ==>>" + e.getMessage());
        }

    }
    //获取已安装应用的 uid，-1 表示未安装此应用或程序异常
    public static int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                Log.d("ProcessUtil", "uid: " + applicationInfo.uid);
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     * Created by cafeting on 2017/2/4.
     *
     * @param context 上下文
     * @param uid     已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(Integer.MAX_VALUE);
        Log.d("ProcessUtil", "isProcessRunning size: " + runningServiceInfos.size());
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos) {
                Log.d("ProcessUtil", "isProcessRunning: " + appProcess.uid + " " + appProcess.process);
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }

}
