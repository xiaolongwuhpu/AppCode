package com.longwu.appcode.acc;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class AppBackToDeskAccessibilityService extends AccessibilityService {
    private String mLastPackageName = "";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("RESTARTED", "onAccessibilityEvent  " + mLastPackageName + " to " + event);
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName().toString();
            if (!packageName.equals(mLastPackageName)) {
                // 前台窗口变更，处理逻辑
                if (!packageName.equals(getPackageName())) {
                    // 前台窗口不是当前应用，可以在这里进行相应的操作
                    Log.d("RESTARTED", "Exit from " + mLastPackageName + " to " + packageName);
                }
                mLastPackageName = packageName;
            }
        }
    }

    @Override
    public void onInterrupt() {
        // Do nothing
    }

//    @Override
//    protected void onServiceConnected() {
//        super.onServiceConnected();
//        // 设置服务为自启动模式，以确保服务在用户重启设备后能够自动启动
//        try {
//            AccessibilityServiceInfo info = getServiceInfo();
//            info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
//            setServiceInfo(info);
//        } catch (Exception e) {
//
//        Log.d("RESTARTED", "error====  "+e.getMessage());
//        }
//        Log.d("RESTARTED", "onServiceConnected  ");
//    }

    @Override
    protected void onServiceConnected() {
        Log.d("RESTARTED", "onServiceConnected  ");
//        sInstance = this;
//        if (SPHelper.isShowWindow(this)) {
//            NotificationActionReceiver.showNotification(this, false);
//        }
//        sendBroadcast(new Intent(QuickSettingTileService.ACTION_UPDATE_TITLE));
        super.onServiceConnected();
    }
}
