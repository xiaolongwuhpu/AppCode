package com.longwu.appcode.util;

import android.os.Handler;
import android.os.HandlerThread;


public class BackgroundThread extends HandlerThread {

    private static volatile BackgroundThread sInstance;
    private static Handler sHandler;

    private BackgroundThread() {
        super("bgThread", HandlerThread.NORM_PRIORITY);

    }

    private static void ensureThreadLocked() {
        if(sInstance == null){
            synchronized (BackgroundThread.class){
                if (sInstance == null) {
                    sInstance = new BackgroundThread();
                    sInstance.start();
                    sHandler = new Handler(sInstance.getLooper());
                }
            }
        }

    }

    public static Handler getHandler() {
        ensureThreadLocked();
        return sHandler;
    }

    public static void post(final Runnable runnable) {
        ensureThreadLocked();
        sHandler.post(runnable);
    }

    public static void postDelayed(final Runnable runnable, long nDelay) {
        ensureThreadLocked();
        sHandler.postDelayed(runnable, nDelay);
    }

    public static void removeTask(final Runnable runnable) {
        ensureThreadLocked();
        sHandler.removeCallbacks(runnable);
    }

}