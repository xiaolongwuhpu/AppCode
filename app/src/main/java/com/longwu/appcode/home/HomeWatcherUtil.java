package com.longwu.appcode.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class HomeWatcherUtil {
    private static final String TAG = "HomeWatcherUtil";
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerRecevier mRecevier;

    // 回调接口
    public interface OnHomePressedListener {
        public void onHomePressed();

        public void onHomeLongPressed();
    }

    public HomeWatcherUtil(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mRecevier = new InnerRecevier();
    }

    /**
     * 开始监听，注册广播
     */
    public void startWatch() {
        if (mRecevier != null && mContext != null) {
            mContext.registerReceiver(mRecevier, mFilter);
        }
    }

    /**
     * 停止监听，注销广播
     */
    public void stopWatch() {
        if (mRecevier != null && mContext != null) {
            mContext.unregisterReceiver(mRecevier);
        }
    }

    /**
     * 广播接收者
     */
    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        long homeTime = 0;
        long recentTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    return;
                }
                Log.i(TAG, "action:" + action + ",reason:" + reason);
                if (mListener == null) {
                    return;
                }

                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    // 短按home键
                    if (isCanCallBack(homeTime)) {
                        mListener.onHomePressed();
                        homeTime = System.currentTimeMillis();
                    } else {
                        Log.i(TAG, "连续触发 home");
                    }
                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    // recent键
                    if (isCanCallBack(recentTime)) {
                        mListener.onHomeLongPressed();
                        recentTime = System.currentTimeMillis();
                    } else {
                        Log.i(TAG, "连续触发 recent");
                    }
                }
            }
        }
    }

    /**
     * 防止500ms内连续触发多次
     *
     * @param time
     * @return
     */
    private boolean isCanCallBack(long time) {
        return System.currentTimeMillis() - time > 500;
    }
}
